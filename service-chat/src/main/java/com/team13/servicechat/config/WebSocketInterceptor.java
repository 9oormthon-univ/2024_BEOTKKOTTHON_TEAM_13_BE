package com.team13.servicechat.config;

import com.team13.servicechat.dto.ChatSessionDto;
import com.team13.servicechat.dto.JwtPayloadDto;
import com.team13.servicechat.service.ChatSessionService;
import com.team13.servicechat.service.JwtService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JwtService jwtService; // JWT 토큰 관리를 위한 서비스

    // NOTE: 채팅 세션 관리를 위한 서비스
    private final ChatSessionService chatSessionService;


    @Override
    public Message<?> preSend(@NonNull  Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // SUBSCRIBE 메시지만 토큰 검증 진행
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {

            // 토큰 가져오기
            String token = accessor.getFirstNativeHeader("Authorization");

            // JWT 토큰 검증
            if (!chatSessionService.isValid(token)) {
                throw new RuntimeException("올바르지 않은 토큰입니다.");
            }

            // 해당 세션의 유저 ID와 이름 추가
            Optional<ChatSessionDto> opSession = chatSessionService.getChatSession(token);

            // NOTE: 세션에 저장된 유저 데이터를 가져올 수 있는 경우에만 메시지 정보 주입
            if (opSession.isPresent()) {
                // STOMP 패킷 헤더에 유저 ID와 유저 이름 추가
                accessor.addNativeHeader("userId", opSession.get().getUsersId());
                accessor.addNativeHeader("userName", opSession.get().getUsersName());

                // 유저 ID를 추가한 새로운 메시지를 생성하고 이를 반환함
                message = MessageBuilder.createMessage(message.getPayload(), accessor.toMessageHeaders());
            }
        }

        return message;
    }
}
