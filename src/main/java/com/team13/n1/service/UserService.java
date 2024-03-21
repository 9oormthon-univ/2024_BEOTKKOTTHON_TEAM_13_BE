package com.team13.n1.service;

import com.team13.n1.entity.User;
import com.team13.n1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    // 해당 유저 ID가 있는지 여부
    public boolean existsById(String userId) {
        return repository.existsById(userId);
    }

    // 새로운 유저 등록
    public void addUser(String userId, String nickname) {
        repository.save(new User(userId, nickname));
    }
}
