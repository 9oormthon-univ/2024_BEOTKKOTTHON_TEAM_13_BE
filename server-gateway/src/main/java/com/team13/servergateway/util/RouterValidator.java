package com.team13.servergateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {

    // 로그인 토큰이 요구되는 path
    private final List<List<String>> securedApiEndpoints = List.of(
            // NOTE: 유저 서비스
            List.of("GET", "/api2/user/mypage/info"),
            List.of("GET", "/api2/user/mypage/recipes"),
            List.of("GET", "/api2/user/mypage/likeRecipes"),
            List.of("GET", "/api2/user/users"),
            // NOTE: 레시피 서비스
            List.of("POST", "/api2/recipe/recipes"),
            List.of("GET", "/api2/recipe/recipes"),
            List.of("POST", "/api2/recipe/recipes/like"), //동적 처리 {recipeId} 인식 위해서
            List.of("GET", "/api2/recipe/recipes/like"),
            // NOTE: 채팅 서비스
            List.of("GET", "/api2/chat/chatroom"),
            List.of("GET", "/api2/chat/chatroom/list"),
            List.of("POST", "/api2/chat/chatroom"),
            List.of("POST", "/api2/chat/chatroom/join")
    );

    // 만약 request의 path가 securedApiEndpoints 중 하나인 경우 true를 반환하고, 그렇지 않으면 false를 반환함
    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        return securedApiEndpoints.stream().anyMatch(methodAndPath ->
                request.getMethod().toString().equals(methodAndPath.get(0)) &&
                        (
                                path.equals(methodAndPath.get(1)) ||
                                (path.startsWith("/api2/recipe/recipes/like/") && methodAndPath.get(1).equals("/api2/recipe/recipes/like"))
                        )
        );
    }
}
