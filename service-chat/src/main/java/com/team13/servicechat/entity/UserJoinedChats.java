package com.team13.servicechat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "user_joined_chats")
public class UserJoinedChats {
    @Id
    private String usersId;

    @Field("chatrooms_ids")
    private List<String> chatroomsIds;
}
