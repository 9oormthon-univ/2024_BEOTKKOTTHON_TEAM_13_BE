package com.team13.n1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.team13.n1.dto.MessageDto;
import com.team13.n1.dto.WSSession;
import com.team13.n1.wspacket.MessagePacket;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
@EnableScheduling
public class ChatWSService {
    private final UserSessionService sessService;
    private final ChatService chatService;
    private final ChatBriefService chatBriefService;
    private final ChatUnreadMessagesService unreadMsgsService;

    private final ObjectMapper mapper;

    private final Map<String, List<WSSession>> chatRooms = new HashMap<>();

    // 유저가 새로 참여한 경우 해당 채팅방에 참여 메시지 전달
    public void join(String chatId, String userId, String nickname) {
        MessagePacket responsePacket = MessagePacket.builder()
                .type(MessagePacket.MessageType.NOTICE)
                .message(nickname + "님이 참여하였습니다.")
                .build();

        broadcast(chatId, userId, responsePacket);
    }

    // 유저 세션 입장
    public void enter(WebSocketSession session, MessagePacket packet) {
        if (packet.getSessionId() != null && sessService.existsById(packet.getSessionId())) {
            String userId = sessService.getUserIdBySessionId(packet.getSessionId());
            String chatId = packet.getChatId();

            // enter를 통해 유저의 세션이 들어오기 전, chatService 내의 채팅방에 유저의 ID가 먼저 등록되어야 함
            if (chatService.existsUserIdInChat(chatId, userId)) {
                // 채팅방이 존재하지 않는 경우, 채팅방을 새로 생성
                if (!chatRooms.containsKey(chatId))
                    chatRooms.put(chatId, new ArrayList<>());

                chatRooms.get(chatId).add(new WSSession(userId, session));
                unreadMsgsService.removeAllMessagesInChat(userId, chatId); // 채팅방에 들어온 경우, 해당 채팅방의 미확인 메시지를 삭제
                return;
            }
        }

        // 미인증된 세션의 경우 메시지 반환
        MessagePacket responsePacket = MessagePacket.builder()
                .type(MessagePacket.MessageType.ERROR)
                .chatId(packet.getChatId())
                .message("올바르지 않은 접근입니다.")
                .build();
        sendMessage(session, responsePacket);
    }

    public void message(WebSocketSession session, MessagePacket packet) {
        if (packet.getSessionId() != null && sessService.existsById(packet.getSessionId())) {
            String userId = sessService.getUserIdBySessionId(packet.getSessionId());
            String chatId = packet.getChatId();

            if (chatService.existsUserIdInChat(chatId, userId)) {
                broadcast(chatId, userId, packet);
                return;
            }
        }

        // 미인증된 세션의 경우 메시지 반환
        MessagePacket responsePacket = MessagePacket.builder()
                .type(MessagePacket.MessageType.ERROR)
                .chatId(packet.getChatId())
                .message("올바르지 않은 접근입니다.")
                .build();
        sendMessage(session, responsePacket);
    }

    // 유저가 해당 채팅방에서 퇴장한 경우 (세션 퇴장과 다름)
    public void userExit(String sessionId, String userId, String chatId) {
        if (chatRooms.containsKey(chatId) && sessService.existsById(sessionId)) {
            MessagePacket noticePacket = MessagePacket.builder()
                    .type(MessagePacket.MessageType.NOTICE)
                    .message(userId + "님이 퇴장하였습니다.")
                    .build();
            broadcast(chatId, userId, noticePacket);

            MessagePacket packet = MessagePacket.builder().sessionId(sessionId).chatId(chatId).build();
            sessionExit(packet);
        }
    }

    // 유저의 세션이 해당 채팅방에서 나간 경우 (유저 퇴장과 다름)
    public void sessionExit(MessagePacket packet) {
        String chatId = packet.getChatId();
        if (packet.getSessionId() != null && chatRooms.containsKey(chatId) &&
                sessService.existsById(packet.getSessionId())) {
            String userId = sessService.getUserIdBySessionId(packet.getSessionId());

            // 채팅방에서 해당 유저의 세션을 삭제함
            int i = 0;
            for (WSSession wsSession : chatRooms.get(chatId)) {
                if (wsSession.getUserId().equals(userId)) {
                    chatRooms.get(chatId).remove(i);
                    break;
                }
                i += 1;
            }

            // 채팅 방 내에 남아있는 세션이 없다면, 해당 채팅방(세션) 삭제
            // 만약 세션이 돌아온 경우 새로 생성됨
            if (chatRooms.get(chatId).isEmpty()) {
                chatRooms.remove(chatId);
            }
        }
    }

    // 공동구매 완료
    public void complete(WebSocketSession session, MessagePacket packet) {
        message(session, packet);
        chatRooms.remove(packet.getChatId()); // 채팅방(세션) 삭제
        chatService.complete(packet.getChatId()); // 채팅방 삭제
    }

    // 특정 세션에게 메시지 전송
    private <T> void sendMessage(WebSocketSession session, T message) {
        // 메시지 전송
        try {
            if (session.isOpen()) // 세션이 열려 있을 경우에만 메시지를 전송함
                session.sendMessage(new TextMessage(mapper.writeValueAsString(message))); // 메시지 내용을 문자열로 변환하여 메시지를 전송함
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        log.info(session.getId() + " <- " + message);
    }

    // 채팅방 내의 전체 유저에게 메시지 전송
    private void broadcast(String chatId, String userId, MessagePacket packet) {
        if (chatRooms.containsKey(chatId)) {
            List<String> userIdsInChat = chatService.getUserIdsInChat(chatId);
            // TODO: 유저 닉네임 및 프로필 사진 추가
            chatService.addMessage(chatId, new MessageDto("윤준영", "/user-image/profile/ypjun100.png",
                    packet.getType(), packet.getMessage()));

            for (WSSession wsSession : chatRooms.get(chatId)) {
                // TODO: 유저 닉네임 및 프로필 사진 추가
                packet.setNickname("윤준영");
                packet.setProfileImage("/user-image/profile/ypjun100.png");
                sendMessage(wsSession.getWsSession(), packet);
                userIdsInChat.remove(wsSession.getUserId()); // 메시지를 전송한 유저는 삭제하고 남은 유저는 읽지 않은 메시지로 전송
            }

            if (packet.getType() == MessagePacket.MessageType.MESSAGE_TEXT ||
                    packet.getType() == MessagePacket.MessageType.MESSAGE_IMAGE ||
                    packet.getType() == MessagePacket.MessageType.NOTICE) {
                // 해당 채팅방의 chat_brief의 last_message를 해당 메시지로 반영
                chatBriefService.updateLastMessage(chatId, packet.getMessage());

                for (String userIdInChat : userIdsInChat) {
                    unreadMsgsService.addMessage(userIdInChat, chatId, packet.getMessage());
                }
            }
        }
    }

    // 10분 주기마다 퇴장한 세션들을 정리함
    @Scheduled(fixedDelay = 10 * 60 * 1000) // 10분 주기
    public void sessionGarbageCollector() {
        for (String chatId : chatRooms.keySet()) {
            // 만약 채팅 세션이 모두 비어있다면, 해당 채팅방 세션 목록 제거
            if (chatRooms.get(chatId).isEmpty()) {
                chatRooms.remove(chatId);
                continue;
            }

            List<WSSession> beRemoved = new ArrayList<>();
            for (WSSession wsSession : chatRooms.get(chatId)) {
                if (!wsSession.getWsSession().isOpen()) {
                    beRemoved.add(wsSession);
                }
            }
            chatRooms.get(chatId).removeAll(beRemoved);
        }
    }
}
