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
@Document(collection = "user_joined_chats")
public class UserJoinedChats {
    @Id
    private Long id; // 유저 ID

    @Field("chatrooms_ids")
    private List<String> chatroomIds;

    // 채팅방 목록에 채팅방 ID 저장
    public void addChatroomId(String chatroomId) {
        if (chatroomId != null) {
            chatroomIds.add(chatroomId);
        }
    }
}
