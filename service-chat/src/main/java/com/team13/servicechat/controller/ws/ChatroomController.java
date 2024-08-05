package com.team13.servicechat.controller.ws;

import com.team13.servicechat.dto.ChatroomMessage;
import com.team13.servicechat.service.ws.ChatroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService service;

    private final SimpMessagingTemplate template;

    // 전체 메시지 관리 핸들러
    @MessageMapping("/chatroom/{chatroomId}")  // 클라이언트 -> 서버 ('/ws/publish/chatroom/{chatroomId}')
    public void messageHandler(@DestinationVariable String chatroomId,
                        @Payload ChatroomMessage message,
                        SimpMessageHeaderAccessor accessor) {
        log.info("IN : " + message);

        // 유저 ID나 이름을 불러올 수 없다면 해당 메시지를 처리하지 않음
        if (accessor.getNativeHeader("userId") == null ||
            accessor.getNativeHeader("userName") == null)
            return;

        // 메시지에 유저 ID와 이름을 메시지에 저장
        message.setSenderUserId(Objects.requireNonNull(accessor.getNativeHeader("userId")).get(0));
        message.setSenderUserName(Objects.requireNonNull(accessor.getNativeHeader("userName")).get(0));

        // 채팅방 내의 사용자들에게 전달될 메시지
        Optional<ChatroomMessage> response = Optional.empty();

        // 메시지 타입에 따른 서비스 라우팅
        if (message.getType() == ChatroomMessage.MessageType.ENTER) {
            response = service.messageEnter(chatroomId, message);
        }

        // 유효한 메시지인 경우에만 채팅방 내 사용자에게 메시지를 전달함
        if (response.isPresent()) {
            log.info("OUT : " + response);

            template.convertAndSend("/ws/subscribe/chatroom/" + chatroomId, response);
        }
    }
}
