package com.team13.servicepost.dto;

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

    public static <T> MyPostListDto<T> from(UserDto userDto, List<T> posts) {
        return MyPostListDto.<T>builder()
                .userId(userDto.getId())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .userRating(userDto.getUserRating())
                .posts(posts)
                .build();
    }
}
