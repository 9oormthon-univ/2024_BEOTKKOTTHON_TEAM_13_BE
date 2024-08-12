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
@Document(collection = "chatrooms")
public class Chatroom {
    @Id
    private String id;

    @Field("posts_id")
    private long postId;

    @Field("users_ids")
    private List<Long> userIds;

    @Field("last_message")
    private String lastMessage;

    @Field("messages")
    private List<Long> messageIds;

    // 유저 목록에 유저 ID 저장
    public void addUserId(Long userId) {
        if (userId != null) {
            userIds.add(userId);
        }
    }
}
