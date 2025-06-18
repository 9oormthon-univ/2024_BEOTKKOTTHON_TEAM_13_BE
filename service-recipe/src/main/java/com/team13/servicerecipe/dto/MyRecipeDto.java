package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyRecipeDto {
    private Long id;
    private String title;
    private int likesCount;
    private String thumbnailImagePath;

    public static MyRecipeDto from(Recipe recipe) {
        return MyRecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .likesCount(recipe.getLikesCount())
                .thumbnailImagePath(recipe.getThumbnailImagePath())
                .build();
    }
}
