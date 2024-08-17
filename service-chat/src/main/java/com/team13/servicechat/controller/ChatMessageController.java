package com.team13.servicechat.controller;

import com.team13.servicechat.dto.ChatMessageDto;
import com.team13.servicechat.service.ChatroomService;
import com.team13.servicechat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService service;                    // 채팅방 웹 소켓 서비스
    private final ChatroomService chatroomService;           // 채팅방 관련 서비스

    private final SimpMessagingTemplate template;            // 소켓 메시지 전송을 위한 템플릿

    @SubscribeMapping("/{chatroomId}")
    public void subscribe(@DestinationVariable String chatroomId,
                                 SimpMessageHeaderAccessor accessor) {

        // 유저 ID나 이름을 불러올 수 없다면 해당 메시지를 처리하지 않음
        if (accessor.getNativeHeader("userId") == null ||
            accessor.getNativeHeader("userName") == null)
            return;

        String userId = Objects.requireNonNull(accessor.getNativeHeader("userId")).get(0);
        String userName = Objects.requireNonNull(accessor.getNativeHeader("userName")).get(0);

        service.subscribe(chatroomId, Long.valueOf(userId), userName, accessor.getSessionId());
    }


    // 전체 메시지 관리 핸들러
    @MessageMapping("/{chatroomId}")  // 클라이언트 -> 서버 ('/ws/publish/chatroom/{chatroomId}')
    public void message(@DestinationVariable String chatroomId,
                        @Payload ChatMessageDto message,
                        SimpMessageHeaderAccessor accessor) {

        log.info("IN : " + message);

        // 채팅방 내의 사용자들에게 전달될 메시지
        Optional<ChatMessageDto> response = Optional.empty();

        // 메시지 타입에 따른 서비스 라우팅
        if (message.getType() == ChatMessageDto.MessageType.MESSAGE_TEXT ||
            message.getType() == ChatMessageDto.MessageType.MESSAGE_IMAGE ) {
            response = service.messageTextAndImage(chatroomId, message, accessor.getSessionId());

        } else if (message.getType() == ChatMessageDto.MessageType.EXIT_USER) {
            response = service.messageExitUser(chatroomId, message, accessor.getSessionId());

        } else if (message.getType() == ChatMessageDto.MessageType.COMPLETE) {
            response = service.messageComplete(chatroomId, message, accessor.getSessionId());

        }

        // 전달할 메시지가 있는 경우에만 채팅방 내 사용자에게 메시지를 전달함
        if (response.isPresent()) {

            // 메시지 저장
            chatroomService.saveMessage(chatroomId, response.get());

            // MySQL 서버에 메시지 저장
//            ChatMessage savedMessage = chatroomService.saveMessage(ChatMessage.builder()
//                            .chatroomId(chatroomId)
//                            .type(response.get().getType().toString())
//                            .message(response.get().getMessage())
//                            .senderUserId(response.get().getSenderUserId())
//                            .senderUserName(response.get().getSenderUserName())
//                            .build());
//
//            // MongoDB 서버에 해당 메시지의 ID 저장
//            service.addMessageIdInChatroom(chatroomId, savedMessage);

            template.convertAndSend("/ws/subscribe/" + chatroomId, response);

            log.info("OUT : " + response);
        }
    }
}
