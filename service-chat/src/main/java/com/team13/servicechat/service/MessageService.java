package com.team13.servicechat.service;

import com.team13.servicechat.dto.MessageDto;
import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.repository.ChatroomRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class MessageService {

    // 접속되어 있는 유저의 정보를 담기위한 레코드
    private record User(@NonNull Long userId, @NonNull String userName, @NonNull String sessionId) { }

    // 채팅방 메시지 데이터를 저장하는 레포지토리
    private final ChatroomRepository repository;

    // 각 채팅방 내에 접속 중인 유저 정보를 저장하는 해쉬맵
    private final Map<String, List<User>> chatrooms = new HashMap<>();



    // SUBSCRIBE 메시지 관리
    public void subscribe(String chatroomId, Long userId, String userName, String sessionId) {
        verify(chatroomId, userId, userName, sessionId);
    }


    // 채팅 메시지
    public Optional<MessageDto> messageTextAndImage(String chatroomId,
                                                    MessageDto message,
                                                    String sessionId) {

        // 유효한 채팅방 ID, 유저인 경우에만 메시지를 반환함 (반환한다는 의미는 이 메시지를 DB에 저장한다는 의미임)
        if (verify(chatroomId, message, sessionId)) {
            // 유저 이름을 가져와 메시지에 저장
            User sender = getUserInChatrooms(chatroomId, sessionId);
            message.setSenderUserId(sender.userId);
            message.setSenderUserName(sender.userName);

            // TODO: feign을 통한 다른 서비스들에게도 채팅방 메시지 전달

            return Optional.of(message);
        }

        return Optional.empty();
    }


    // 유저의 공동구매 포기
    public Optional<MessageDto> messageExitUser(String chatroomId,
                                                MessageDto message,
                                                String sessionId) {

        // 유효한 채팅방 ID, 유저인 경우에만 메시지를 반환함 (반환한다는 의미는 이 메시지를 DB에 저장한다는 의미임)
        if (verify(chatroomId, message, sessionId)) {
            // 유저 이름을 가져와 메시지에 저장
            User sender = getUserInChatrooms(chatroomId, sessionId);
            message.setSenderUserId(sender.userId);
            message.setSenderUserName(sender.userName);

            // 채팅방 데이터를 가져옴
            Chatroom chatroom = repository.findById(chatroomId).orElseThrow();

            // 해당 채팅방에서 유저가 포함되어 있는지 확인
            int userIndex = chatroom.getUserIds().indexOf(message.getSenderUserId());

            if (userIndex != -1) {
                // 퇴장 유저 정보를 채팅방 내에서 제거
                chatroom.getUserIds().remove(userIndex);

                // 만약 모든 유저가 채팅방에서 나간 경우, 해당 채팅방 제거
                if (chatroom.getUserIds().isEmpty()) {
                    repository.deleteById(chatroomId);
                    chatrooms.remove(chatroomId);

                    // TODO: feign을 통한 다른 서비스들에게도 채팅방 삭제 메시지 전달

                    repository.deleteById(chatroomId);

                    return Optional.empty();
                }

                return Optional.of(MessageDto.builder()
                                .type(MessageDto.MessageType.NOTICE)
                                .message(message.getSenderUserName() + "님이 공동구매를 포기하셨습니다.")
                                .build());
            }
        }

        return Optional.empty();
    }


    // TODO: 공동구매 완료
    public Optional<MessageDto> messageComplete(String chatroomId,
                                                MessageDto message,
                                                String sessionId) {
       /*
       공동구매 완료 기능은 공동구매 게시자만 요청할 수 있으며, 게시자가 완료 요청시 해당 메시지를 전체 클라이언트에게 전송합니다.
       해당 메시지를 받은 다른 사용자들은 리뷰를 작성하면 messageExitUser() 메서드를 통해 채팅방을 나갈 수 있으며, 모든 유저가
       채팅방을 나간 경우 해당 채팅방 정보를 chatrooms과 DB 상에서 제거한다.
        */

        return Optional.empty();
    }


    // TODO: 공동구매 참여시 채팅방 내에 있는 유저들에게 '~님이 공동구매에 참여하였습니다.' 메시지 전달


    // 채팅방에 해당 메시지 아이디 추가
    public void addMessageIdInChatroom(String chatroomId, ChatMessage message) {
        if (verifyChatroomId(chatroomId)) {
            Chatroom chatroom = repository.findById(chatroomId).orElseThrow();

            // 해당 메시지를 저장한 뒤, 채팅방의 마지막 메시지를 해당 메시지로 설정
            chatroom.getMessageIds().add(message.getId());
            chatroom.setLastMessage(message.getMessage());

            repository.save(chatroom);
        }
    }


    /*
    verify~() 메서드는 올바른 채팅방인지 혹은 특정 채팅방에 권한이 있는 사용자인지를 판별합니다.
    이 메서드들은 먼저 클래스 내부에 선언된 chatrooms 해쉬맵을 통해 유효한지 판별하게 되고, 만약 해쉬맵을
    통해 확인하지 못한다면, repository를 통해 다시 한번 유효한지 판별하게 됩니다. 또한 repository의
    정보가 chatrooms에 반영되지 않은 경우 이를 반영하여 최대한 repository에 접근하는 것을 방지하고
    chatrooms를 캐쉬 용도로 사용할 수 있게끔 합니다.
     */

    // 존재하는 채팅방 ID인지 확인
    private boolean verifyChatroomId(String chatroomId) {

        if (chatrooms.containsKey(chatroomId)) {
            return true;
        } else if (repository.existsById(chatroomId)) {
            chatrooms.put(chatroomId, new ArrayList<>());  // 유효한 채팅방인지 확인함과 동시에 chatrooms에 채팅방을 추가시킴
            return true;
        }

        return false;
    }


    // 사용자가 전달한 chatroomId와 userId가 올바른 아이디인지 확인
    // 만약 존재하지 않는 채팅방이거나, 해당 채팅방에 대한 권한이 없는 유저의 경우 false를 반환함
    private boolean verify(String chatroomId, MessageDto message, String sessionId) {

        Long userId = message.getSenderUserId();
        String userName = message.getSenderUserName();

        return verify(chatroomId, userId, userName, sessionId);
    }

    private boolean verify(String chatroomId, Long userId, String userName, String sessionId) {

        // 존재하는 채팅방 ID인지 확인
        if (verifyChatroomId(chatroomId)) {
            // chatrooms 내에 등록된 세션 ID인지 확인
            if (chatrooms.get(chatroomId).stream().anyMatch((user) -> user.sessionId.equals(sessionId))) {
                return true;
            }

            // repository 내에서 존재하는 유저 ID인지 확인
            List<Long> users = repository.findById(chatroomId).orElseThrow().getUserIds();
            if (users.contains(userId)) {
                chatrooms.get(chatroomId).add(new User(userId, userName, sessionId));
                return true;
            }
        }

        return false;
    }


    // chatrooms 내에서 특정 sessionId를 갖는 유저 객체 반환
    private User getUserInChatrooms(String chatroomId, String sessionId) {
        return chatrooms.get(chatroomId).stream()
                .filter(user -> user.sessionId.equals(sessionId))
                .findFirst()
                .orElseThrow();
    }
}
