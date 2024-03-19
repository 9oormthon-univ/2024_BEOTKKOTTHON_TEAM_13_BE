package com.team13.n1.service;

import com.team13.n1.entity.UserSession;
import com.team13.n1.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService {
    private final UserSessionRepository repository;

    // 해당 세션 ID가 존재하는지 확인
    public boolean existsById(String sessionId) {
        return repository.existsById(sessionId);
    }

    // 해당 세션의 유저 ID값을 반환
    public String getUserIdBySessionId(String sessionId) {
        Optional<UserSession> userSession = repository.findById(sessionId);
        if (userSession.isPresent()) {
            return userSession.get().getUserId();
        }
        return "";
    }
}
