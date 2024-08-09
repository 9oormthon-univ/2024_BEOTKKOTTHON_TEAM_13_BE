package com.team13.servicechat.repository;

import com.team13.servicechat.entity.ChatroomMessages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomMessagesRepository extends CrudRepository<ChatroomMessages, Long> {
}
