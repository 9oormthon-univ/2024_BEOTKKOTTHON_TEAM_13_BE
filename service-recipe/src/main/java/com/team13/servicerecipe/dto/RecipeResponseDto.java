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
public class RecipeResponseDto {
    private Long id;
    private Long userId;
    private String userNickname;
    private String userProfileUrl;
    private String title;
    private String contents;
    private String thumbnailImagePath;
    private List<RecipeIngredientDto> ingredients;
    private List<RecipeProcessDto> processes;
}