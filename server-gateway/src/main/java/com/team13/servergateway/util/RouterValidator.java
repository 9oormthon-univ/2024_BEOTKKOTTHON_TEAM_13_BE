package com.team13.servergateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class RouterValidator {

    // 로그인 토큰이 요구되는 path
    private final List<List<Pattern>> securedApiEndpoints = List.of(
            // NOTE: 유저 서비스
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/mypage/info$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/mypage/recipes$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/mypage/likeRecipes$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/users$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/mypage/posts$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/user/mypage/likePosts$")),
            // NOTE: 레시피 서비스
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/recipe/recipes$")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/recipe/recipes$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/recipe/recipes/like/")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/recipe/recipes/like/\\d+$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/recipe/comments/")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/recipe/comments/\\d+$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/recipe/comments/replies$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/recipe/comments/user$")),
            // NOTE: 채팅 서비스
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/chat/chatroom$")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/chat/chatroom$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/chat/chatroom/list$")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/chat/chatroom/join$")),
            // NOTE: 공동구매 서비스
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/post/posts$")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/post/posts$")),
            List.of(Pattern.compile("GET"), Pattern.compile("^/api2/post/posts/like/")),
            List.of(Pattern.compile("POST"), Pattern.compile("^/api2/post/posts/like/"))
    );

    // 만약 request의 path가 securedApiEndpoints 중 하나인 경우 true를 반환하고, 그렇지 않으면 false를 반환함
    public boolean isSecured(ServerHttpRequest request) {
        String method = request.getMethod().toString();
        String path = request.getURI().getPath();

        return securedApiEndpoints.stream().anyMatch(methodAndPath ->
                methodAndPath.get(0).matcher(method).matches() && methodAndPath.get(1).matcher(path).matches());
    }
}
