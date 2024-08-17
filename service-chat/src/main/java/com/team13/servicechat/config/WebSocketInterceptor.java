package com.team13.servicechat.config;

import com.team13.servicechat.dto.JwtPayloadDto;
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

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JwtService jwtService; // JWT 토큰 관리를 위한 서비스


    @Override
    public Message<?> preSend(@NonNull  Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // SUBSCRIBE 메시지만 JWT 검증 진행
        if (accessor.getCommand() == StompCommand.SUBSCRIBE) {

            // JWT 토큰 가져오기
            String token = accessor.getFirstNativeHeader("Authorization");

            // "LTK=" 접두사가 포함되어 있다면 제거함
            if (Objects.requireNonNull(token).startsWith("LTK=")) {
                token = token.substring(4);
            }

            // JWT 토큰 검증
            if (!jwtService.verifyToken(token)) {
                throw new RuntimeException("올바르지 않은 토큰입니다.");
            }

            // JWT 토큰에서 페이로드 추출
            JwtPayloadDto payload = jwtService.getPayloadFromToken(token);

            // STOMP 패킷 헤더에 유저 ID와 유저 이름 추가
            accessor.addNativeHeader("userId", payload.getUserId());
            accessor.addNativeHeader("userName", payload.getUserNickname());

            // 유저 ID를 추가한 새로운 메시지를 생성하고 이를 반환함
            message = MessageBuilder.createMessage(message.getPayload(), accessor.toMessageHeaders());
        }

        return message;
    }
}
