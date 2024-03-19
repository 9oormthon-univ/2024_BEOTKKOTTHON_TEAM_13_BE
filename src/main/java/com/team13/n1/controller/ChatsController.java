package com.team13.n1.controller;

import com.team13.n1.dto.ChatBriefDto;
import com.team13.n1.dto.ChatUnreadMessageDto;
import com.team13.n1.entity.Chat;
import com.team13.n1.entity.ChatBrief;
import com.team13.n1.dto.MessageDto;
import com.team13.n1.entity.ChatUnreadMessages;
import com.team13.n1.entity.UserJoinedChats;
import com.team13.n1.service.*;
import com.team13.n1.wspacket.MessagePacket;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

// 채팅방 목록 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatsController {
    private final UserSessionService sessService;                       // 세션 관리 서비스
    private final UserJoinedChatsService joinedChatsService;            // 유저가 참여한 채팅방 관리 서비스
    private final ChatService chatService;                              // 채팅방 관리 서비스
    private final ChatBriefService chatBriefService;                    // 채팅방의 마지막 메시지만 담은 관리 서비스
    private final ChatUnreadMessagesService chatUnreadMessagesService;  // 해당 유저의 미확인 메시지 관리 서비스

    // 유저가 참여한 채팅방 목록을 불러옴
    @GetMapping("/list")
    public List<ChatBriefDto> getChats(@RequestParam("session_id") String sessionId) {
        List<ChatBriefDto> result = new ArrayList<>();

        // 유저가 참여하고 있는 채팅방이 있는 경우 해당 채팅방의 ID값으로 chat_brief 데이터를 가져와 사용자에게 전달
        // 이때, chat_brief에는 채팅방 목록에서 보여주기 위한 채팅방 ID, 해당 채팅방의 마지막 메시지, 채팅방과 연결된 게시글 ID가 저장됨
        if (sessService.existsById(sessionId)) {
            String userId = sessService.getUserIdBySessionId(sessionId);
            // userId 데이터가 존재하고, 해당 유저가 참여한 채팅방이 있는 경우 실행
            if (!userId.isEmpty() && joinedChatsService.existsByUserId(userId)) {
                List<String> chatIds = joinedChatsService.getChatsIdsByUserId(userId); // 해당 유저가 참여한 채팅방 ID 리스트
                for (String chatId : chatIds) {
                    ChatBrief chatBrief = chatBriefService.getById(chatId);
                    result.add(new ChatBriefDto(chatBrief.getChatId(), chatBrief.getPostId(), chatBrief.getLastMessage()));
                }
            }
        }

        return result;
    }

    // 사용자가 미확인한 메시지
    @GetMapping("/unread-messages")
    public List<ChatUnreadMessageDto> getUnreadMessages(@RequestParam("session_id") String sessionId) {
        List<ChatUnreadMessageDto> result = new ArrayList<>();

        if (sessService.existsById(sessionId)) {
            String userId = sessService.getUserIdBySessionId(sessionId);
            if (!userId.isEmpty() && chatUnreadMessagesService.existsByUserId(userId)) {
                result.addAll(chatUnreadMessagesService.getUnreadMessagesByUserId(userId));
            }
        }

        return result;
    }

    // 테스트 용으로 임시 데이터 저장
    @PostConstruct
    private void init() {
        List<String> chatIds = new ArrayList<>();
        chatIds.add("abc");

        // ypjun100
        joinedChatsService.save(new UserJoinedChats("ypjun100", chatIds));

        List<String> userIds = new ArrayList<>();
        userIds.add("ypjun100");
        userIds.add("ypjun101");

        List<MessageDto> messageDtos = new ArrayList<>();
        messageDtos.add(new MessageDto("ypjun100", "ypjun100이 보낸 메시지임", MessagePacket.MessageType.MESSAGE_TEXT));
        messageDtos.add(new MessageDto("ypjun101", "이거는 ypjun101이 보낸 메시지임", MessagePacket.MessageType.MESSAGE_TEXT));
        chatService.save(new Chat("abc", "", userIds, messageDtos));

        chatBriefService.save(new ChatBrief("abc", "", "hi2"));

        List<ChatUnreadMessageDto> unreadMessages = new ArrayList<>();
        unreadMessages.add(new ChatUnreadMessageDto("abc", "미확인 메시지1"));
        unreadMessages.add(new ChatUnreadMessageDto("abc", "hi2"));
        chatUnreadMessagesService.save(new ChatUnreadMessages("ypjun100", unreadMessages));

        // ypjun101
        chatIds.add("bcd");
        unreadMessages.add(new ChatUnreadMessageDto("bcd", "미확인 메시지3"));
        chatBriefService.save(new ChatBrief("bcd", "", "미확인 메시지3"));
        joinedChatsService.save(new UserJoinedChats("ypjun101", chatIds));
        chatUnreadMessagesService.save(new ChatUnreadMessages("ypjun101", unreadMessages));
    }
}
