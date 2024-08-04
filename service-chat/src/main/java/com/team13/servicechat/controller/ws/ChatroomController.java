package com.team13.servicechat.controller.ws;

import com.team13.servicechat.dto.ChatroomMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatroomController {

    private final SimpMessagingTemplate template;

    // 전체 메시지 관리 핸들러
    @MessageMapping("/chatroom/{chatroomId}")  // 클라이언트 -> 서버 ('/ws/publish/chatroom/{chatroomId}')
    @SendTo("/chatroom/{chatroomId}")          // 서버 -> 클라이언트 ('/ws/subscribe/chatroom/{chatroomId}')
    public void messageHandler(@DestinationVariable String chatroomId,
                        @Payload ChatroomMessage message,
                        SimpMessageHeaderAccessor accessor) {

        log.info(chatroomId);
        log.info(message);
        log.info(accessor.getNativeHeader("userId"));

        template.convertAndSend("/ws/subscribe/chatroom/" + chatroomId, message);
    }
}
