package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.Recipe;
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
    private List<RecipeCommentDto> comments;

    public static RecipeResponseDto from(Recipe recipe, UserDto userDto,
                                         List<RecipeIngredientDto> ingredients,
                                         List<RecipeProcessDto> processes,
                                         List<RecipeCommentDto> comments) {
        return RecipeResponseDto.builder()
                .id(recipe.getId())
                .userId(recipe.getUserId())
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .title(recipe.getTitle())
                .contents(recipe.getContents())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .ingredients(ingredients)
                .processes(processes)
                .comments(comments)
                .build();
    }
}
