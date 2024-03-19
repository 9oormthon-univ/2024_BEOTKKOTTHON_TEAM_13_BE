package com.team13.n1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "user_joined_chats")
@AllArgsConstructor
public class UserJoinedChats {
    @Id private String userId;
    @Field("chat_ids") private List<String> chatIds;  // 해당 유저가 참여중인 채팅방의 ID값들
}
