package com.team13.servicerecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MypageRecipeResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String contents;
    private int commentCount;
    private String thumbnailImagePath;
    private Date createdAt;
    private int type;
    private String userNickname;
    private List<RecipeIngredientDto> ingredients;
    private Long likesCount;
    private String userProfileUrl;
}