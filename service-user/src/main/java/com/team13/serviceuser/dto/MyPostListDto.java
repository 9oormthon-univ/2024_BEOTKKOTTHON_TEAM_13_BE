package com.team13.serviceuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostListDto<T> {
    private Long userId;
    private String userNickname;
    private String userProfileUrl;
    private float userRating;
    private List<T> posts;

    public MyPostListDto(List<T> posts) {
        this.posts = posts;
    }
}

