package com.team13.n1.service;

import com.team13.n1.entity.User;
import com.team13.n1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
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

    // 유저 ID를 통해 닉네임 불러오기
    public String getNicknameById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get().getNickname();
            }
        }
        return "";
    }

    // 유저 ID를 통해 만족도 불러오기
    public double getUserRatingById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get().getUserRating();
            }
        }
        return 0d;
    }

    // 유저 정보(닉네임, 만족도)
    public Map<String, Object> getUserInfo(String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put("nickname", getNicknameById(userId));
        user.put("user_rating", getUserRatingById(userId));
        return user;
    }
}
