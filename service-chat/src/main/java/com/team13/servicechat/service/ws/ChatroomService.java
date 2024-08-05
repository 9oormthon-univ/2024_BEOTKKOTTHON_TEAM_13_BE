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

}
