package com.team13.servergateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouterValidator {

    // 로그인 토큰이 요구되는 path
    private final List<List<String>> securedApiEndpoints = List.of(
//            List.of("POST", "/recipe")
    );

    // 만약 request의 path가 securedApiEndpoints 중 하나인 경우 true를 반환하고, 그렇지 않으면 false를 반환함
    public boolean isSecured(ServerHttpRequest request) {
        return securedApiEndpoints.stream().anyMatch(methodAndPath ->
                request.getMethod().toString().equals(methodAndPath.get(0)) &&
                request.getURI().getPath().contains(methodAndPath.get(1))
        );
    }
}
