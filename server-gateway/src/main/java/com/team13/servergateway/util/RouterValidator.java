package com.team13.servergateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {

    // 로그인 토큰이 요구되는 path
    private final List<List<String>> securedApiEndpoints = List.of(
            List.of("GET", "/user/config"),
            List.of("GET", "/mypage")
            // 채팅 관련 API는 모두 토큰 요구
//            List.of("GET", "/chat"),
//            List.of("POST", "/chat")
    );

    // 만약 request의 path가 securedApiEndpoints 중 하나인 경우 true를 반환하고, 그렇지 않으면 false를 반환함
    // 로그인 토큰이 필요하지 않은 path
    private final List<List<String>> openApiEndpoints = List.of(
            List.of("POST", "/sign/login"),    // 로그인 요청
            List.of("POST", "/sign/join") // 회원가입 요청
    );

    // 인증이 필요한 경로인지 확인
    public boolean isSecured(ServerHttpRequest request) {
        // 인증이 필요하지 않은 경로라면 false 반환
        boolean isOpenApi = openApiEndpoints.stream().anyMatch(methodAndPath ->
                request.getMethod().toString().equals(methodAndPath.get(0)) &&
                        request.getURI().getPath().contains(methodAndPath.get(1))
        );

        if (isOpenApi) {
            return false;
        }

        // 인증이 필요한 경로인지 확인
        return securedApiEndpoints.stream().anyMatch(methodAndPath ->
                request.getMethod().toString().equals(methodAndPath.get(0)) &&
                        request.getURI().getPath().contains(methodAndPath.get(1))
        );
    }
}
