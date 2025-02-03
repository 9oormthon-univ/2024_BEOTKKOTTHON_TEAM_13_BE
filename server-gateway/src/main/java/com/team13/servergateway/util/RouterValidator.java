package com.team13.servergateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {

    // 로그인 토큰이 요구되는 path
    private final List<List<String>> securedApiEndpoints = List.of(
            // NOTE: 유저 서비스
            List.of("GET", "/api2/user/mypage"),
            List.of("GET", "/api2/user/users"),
            // NOTE: 레시피 서비스
            List.of("POST", "/api2/recipe/recipes"),
            List.of("GET", "/api2/recipe/recipes"),
            // NOTE: 채팅 서비스
            List.of("GET", "/api2/chat/chatroom"),
            List.of("GET", "/api2/chat/chatroom/list"),
            List.of("POST", "/api2/chat/chatroom"),
            List.of("POST", "/api2/chat/chatroom/join"),
            // NOTE: 공동구매 서비스
            List.of("POST", "/api2/post/posts"),
            List.of("GET", "/api2/post/posts")
    );

    // 만약 request의 path가 securedApiEndpoints 중 하나인 경우 true를 반환하고, 그렇지 않으면 false를 반환함
    public boolean isSecured(ServerHttpRequest request) {
        return securedApiEndpoints.stream().anyMatch(methodAndPath ->
                request.getMethod().toString().equals(methodAndPath.get(0)) &&
                        request.getURI().getPath().equals(methodAndPath.get(1))
        );
    }
}
