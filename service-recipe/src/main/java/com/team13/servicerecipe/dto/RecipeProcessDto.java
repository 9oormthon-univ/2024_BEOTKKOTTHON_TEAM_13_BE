package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeProcess;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeProcessDto {
    private Long id;
    private String imagePath;
    private String contents;

    public static RecipeProcessDto from(RecipeProcess entity) {
        return RecipeProcessDto.builder()
                .id(entity.getId())
                .imagePath(entity.getImagePath())
                .contents(entity.getContents())
                .build();
    }

    public static RecipeProcess toEntity(RecipeProcessDto dto, Recipe recipe) {
        return RecipeProcess.builder()
                .id(dto.getId())
                .imagePath(dto.getImagePath())
                .contents(dto.getContents())
                .recipe(recipe)
                .build();
    }
}
