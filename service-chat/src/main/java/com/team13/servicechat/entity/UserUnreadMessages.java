package com.team13.servicechat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "user_unread_messages")
public class UserUnreadMessages {
    @Id
    private Long id; // 유저 ID

    @Field("unread_message")
    private List<ChatMessage> unreadMessages;
}
