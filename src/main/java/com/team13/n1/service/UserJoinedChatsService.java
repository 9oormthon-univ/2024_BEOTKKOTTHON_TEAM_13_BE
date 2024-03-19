package com.team13.n1.service;

import com.team13.n1.entity.UserJoinedChats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserJoinedChatsService {
    private final MongoTemplate template;

    // 유저가 참여한 채팅방이 있는지에 대한 여부
    public boolean existsByUserId(String userId) {
        return template.exists(Query.query(Criteria.where("_id").is(userId)),
                UserJoinedChats.class, "user_joined_chats");
    }

    // 유저가 참여한 채팅방들의 id값 반환
    public List<String> getChatsIdsByUserId(String userId) {
        return Objects.requireNonNull(template.findById(userId, UserJoinedChats.class,
                "user_joined_chats")).getChatIds();
    }

    // 테스트용
    public void save(UserJoinedChats userJoinedChats) {
        template.save(userJoinedChats, "user_joined_chats");
    }
}
