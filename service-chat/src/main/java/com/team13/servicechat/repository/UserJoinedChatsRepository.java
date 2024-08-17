package com.team13.servicechat.repository;

import com.team13.servicechat.entity.UserJoinedChats;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserJoinedChatsRepository extends MongoRepository<UserJoinedChats, Long> {
}
