package com.team13.servicechat.repository;

import com.team13.servicechat.entity.UserUnreadMessages;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserUnreadMsgsRepository extends MongoRepository<UserUnreadMessages, Long> {
}
