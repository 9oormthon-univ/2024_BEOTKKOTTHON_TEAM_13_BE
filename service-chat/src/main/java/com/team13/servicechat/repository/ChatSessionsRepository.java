package com.team13.servicechat.repository;

import com.team13.servicechat.entity.ChatSessions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatSessionsRepository extends MongoRepository<ChatSessions, String> {
    // NOTE: 해당 토큰이 존재하는지 확인
    boolean existsByToken(String token);

    // NOTE: 토큰으로 채팅 세션을 가져옴
    Optional<ChatSessions> findChatSessionsByToken(String token);
}
