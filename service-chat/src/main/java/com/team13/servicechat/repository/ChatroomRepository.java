package com.team13.servicechat.repository;

import com.team13.servicechat.entity.Chatroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepository extends MongoRepository<Chatroom, String> {
}
