package com.team13.servicechat.service.ws;

import com.team13.servicechat.dto.ChatroomMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class ChatroomService {

    // 각 채팅방 ID에 속한 유저 ID 목록
    private final Map<String, List<String>> chatrooms;

    public ChatroomService() {
        chatrooms = new HashMap<>();

        // 테스트 채팅방 개설
        chatrooms.put("test-chatroom", new ArrayList<>());
    }

    // 채팅방 입장
    public Optional<ChatroomMessage> messageEnter(String chatroomId, ChatroomMessage message) {
        // 존재하는 채팅방인 경우 메시지 발신자를 해당 채팅방에 입장시킴
        if (chatrooms.containsKey(chatroomId)) {
            chatrooms.get(chatroomId).add(message.getSenderUserId());

            return Optional.of(ChatroomMessage.builder()
                            .type(ChatroomMessage.MessageType.NOTICE)
                            .message(message.getSenderUserName() + "님이 입장하셨습니다.")
                            .build());
        }

        return Optional.empty();
    }

    // 채팅 메시지
    public Optional<ChatroomMessage> messageTextAndImage(String chatroomId, ChatroomMessage message) {
        // 채팅방이 존재하는 경우에만 메시지를 반환함(반환한다는 의미는 이 메시지를 DB에 저장한다는 의미임)
        if (chatrooms.containsKey(chatroomId)) {
            return Optional.of(message);
        }

        return Optional.empty();
    }

    // 채팅방에서 유저 퇴장
    public Optional<ChatroomMessage> messageExitUser(String chatroomId, ChatroomMessage message) {
        // 나가려는 채팅방이 존재하는지 확인
        if (chatrooms.containsKey(chatroomId)) {
            // 해당 채팅방에서 유저가 포함되어 있는지 확인
            int userIndex = chatrooms.get(chatroomId).indexOf(message.getSenderUserId());

            if (userIndex != -1) {
                // 퇴장 유저 정보를 채팅방 내에서 제거
                chatrooms.get(chatroomId).remove(message.getSenderUserId());

                // 만약 모든 유저가 채팅방에서 나간 경우, 해당 채팅방 제거
                if (chatrooms.get(chatroomId).isEmpty()) {
                    chatrooms.remove(chatroomId);

                    // TODO: DB에서 채팅방 삭제
                }

                return Optional.of(ChatroomMessage.builder()
                                .type(ChatroomMessage.MessageType.NOTICE)
                                .message(message.getSenderUserName() + "님이 퇴장하셨습니다.")
                                .build());
            }
        }

        return Optional.empty();
    }

    // 공동구매 완료
    public Optional<ChatroomMessage> messageComplete(String chatroomId, ChatroomMessage message) {
       /*
       공동구매 완료 기능은 공동구매 게시자만 요청할 수 있으며, 게시자가 완료 요청시 해당 메시지를 전체 클라이언트에게 전송합니다.
       해당 메시지를 받은 다른 사용자들은 리뷰를 작성하면 messageExitUser() 메서드를 통해 채팅방을 나갈 수 있으며, 모든 유저가
       채팅방을 나간 경우 해당 채팅방 정보를 chatrooms과 DB 상에서 제거한다.
        */

        return Optional.empty();
    }
}
