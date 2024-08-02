package com.team13.servicechat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "chatrooms")
public class Chatrooms {
    @Id
    private String id;

    @Field("posts_id")
    private long postsId;

    @Field("users_ids")
    private List<Long> usersIds;

    @Field("last_message")
    private String lastMessage;

    @Field("messages")
    private List<Long> messages;
}
