package com.team13.serviceuser.converter;

import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.entity.User;

public class SignConverter {

    public static User toUserEntity(SignRequest.RegisterRequestDto registerRequestDto, String encodedPassword, String profileImageUrl) {
        return User.builder()
                .email(registerRequestDto.getEmail())
                .password(encodedPassword)
                .nickname(registerRequestDto.getNickname())
                .userRating(0L) // 기본값 설정
                .profileImageUrl(profileImageUrl) // 기본 프로필 이미지 설정
                .build();
    }
}

