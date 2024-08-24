package com.team13.serviceuser.dto;

import com.team13.serviceuser.entity.User;
import lombok.Data;

@Data
public class JoinRequest {
    private String email;
    private String password;
    private String passwordCheck;
    private String nickname;

    // 비밀번호 암호화 X
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .nickname(this.nickname)
                .build();
    }
}
