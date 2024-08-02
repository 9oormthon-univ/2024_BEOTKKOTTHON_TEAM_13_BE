package com.team13.servicechat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "user_unread_messages")
public class UserUnreadMessages {
    @Id
    private String usersId;

    @Field("unread_message")
    private List<Long> unreadMessages;
}
