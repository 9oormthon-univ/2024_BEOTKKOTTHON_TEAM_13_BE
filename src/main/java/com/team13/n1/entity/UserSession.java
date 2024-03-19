package com.team13.n1.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value="user_session", timeToLive = 300)
public class UserSession {
    @Id private String id;  // 세션 ID (웹소켓 세션과 다른 것에 주의)
    private String userId;  // 해당 유저의 ID

    public UserSession(String id, String userId) {
        this.id = id;
        this.userId = userId;
    }
}
