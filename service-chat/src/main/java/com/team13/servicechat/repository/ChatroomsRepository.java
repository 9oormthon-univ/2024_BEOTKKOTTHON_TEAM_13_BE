package com.team13.servicechat.repository;

import com.team13.servicechat.entity.Chatrooms;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomsRepository extends MongoRepository<Chatrooms, String> {
}
