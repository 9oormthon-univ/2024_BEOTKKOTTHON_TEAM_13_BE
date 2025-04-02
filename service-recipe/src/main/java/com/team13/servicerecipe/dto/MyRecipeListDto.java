package com.team13.servicerecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyRecipeListDto {
    private Long userId;
    private String userNickname;
    private String userProfileUrl;
    private float userRating;
    private List<MyRecipeDto> recipes;

    public static MyRecipeListDto from(UserDto userDto, List<MyRecipeDto> recipes) {
        return MyRecipeListDto.builder()
                .userId(userDto.getId())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .userRating(userDto.getUserRating())
                .recipes(recipes)
                .build();
    }
}
