package com.team13.servicechat.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "chat_sessions")
public class ChatSessions {
    @Id
    private String id;

    // NOTE: 사용자가 소켓에 연결하기 위한 토큰
    @Indexed(name = "token")
    private String token;

    @Field(name = "users_id")
    private String users_id;

    @Field(name = "users_name")
    private String users_name;

    @Field(name = "users_profile")
    private String users_profile;

    @Indexed(name = "expire_at", expireAfterSeconds = 0)
    private Date expire_at;
}
