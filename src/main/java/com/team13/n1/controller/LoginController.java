package com.team13.n1.controller;

import com.team13.n1.entity.UserSession;
import com.team13.n1.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final UserSessionRepository userSessionRepo;

    // 사용자 로그인
    @PostMapping("/login")
    public String doLogin(@RequestBody Map<String, String> request) {
        if (request.get("id").equals("ypjun100") || request.get("id").equals("ypjun101") || request.get("id").equals("testid")) {
            String sessionId = UUID.randomUUID().toString();
            userSessionRepo.save(new UserSession(sessionId, request.get("id")));
            return sessionId;
        }

        return "";
    }
}
