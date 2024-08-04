package com.team13.servicechat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.security.Key;

@Log4j2
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private final Key tokenKey; // JWT 토큰을 위한 키

    @Autowired
    public WebSocketInterceptor(@Value("${app.jwt.secret}") String tokenSecret) {
        super();

        // 암호키를 바탕으로 키 생성
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        this.tokenKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Message<?> preSend(@NonNull  Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 특정 패킷에 한해서만 JWT 검증
        if (accessor.getCommand() == StompCommand.CONNECT ||
                accessor.getCommand() == StompCommand.SEND ||
                accessor.getCommand() == StompCommand.SUBSCRIBE) {

            // JWT 토큰 가져오기
            String token = accessor.getFirstNativeHeader("Authorization");

            // JWT 토큰 검증
            if (!this.validateAccessToken(token)) {
                throw new RuntimeException("Access 토큰이 필요합니다.");
            }

            // 클라이언트가 메시지를 보낸 경우 해당 메시지 패킷에 유저 ID 추가
            if (accessor.getCommand() == StompCommand.SEND) {
                // JWT 토큰을 기반으로 유저 ID 불러오기
                String userEmail = getEmailInToken(token);

                // TODO: 에메일을 유저 ID로 변환시키는 코드 추가
                String userId = String.valueOf(userEmail.hashCode());

                // STOMP 패킷 헤더에 유저 ID 추가
                accessor.addNativeHeader("userId", userId);

                // 유저 ID를 추가한 새로운 메시지를 생성하고 이를 반환함
                message = MessageBuilder.createMessage(message.getPayload(), accessor.toMessageHeaders());
            }
        }

        return message;
    }

    // 올바른 JWT 토큰인지 확인
    private boolean validateAccessToken(String token) {
        if (token == null) {
            return false;
        }

        if (!token.trim().isEmpty() && token.startsWith("LTK=")) {
            try {
                token = token.substring(4);

                Jwts.parserBuilder()
                        .setSigningKey(tokenKey)
                        .build()
                        .parseClaimsJws(token);

                return true;
            } catch (Exception e) {
                log.error(e);
                return false;
            }
        }

        return false;
    }

    // 토큰 내에서 이메일 정보를 추출함
    private String getEmailInToken(String token) {
        try {
            token = token.substring(4);

            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(tokenKey)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();

            if (claims.containsKey("email")) {
                return claims.get("email", String.class);
            }

        } catch (Exception e) {
            log.error(e);
        }

        return "";
    }
}
