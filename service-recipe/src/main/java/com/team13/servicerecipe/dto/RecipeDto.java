package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.entity.RecipeProcess;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    private Long id;
    private String userProfileUrl;
    private String userNickname;
    private String title;
    private String contents;
    private int commentCount;
    private int likesCount;
    private String thumbnailImagePath;
    private List<RecipeIngredient> ingredients;
    private List<RecipeProcess> processes;

    public static RecipeDto of(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .contents(recipe.getContents())
                .commentCount(recipe.getCommentCount())
                .likesCount(recipe.getLikesCount())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .build();
    }
}