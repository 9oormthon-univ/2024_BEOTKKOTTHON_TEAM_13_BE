package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeIngredient;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeIngredientDto {
    private Long id;
    private String name;
    private String amount;

    public static RecipeIngredientDto from(RecipeIngredient entity) {
        return RecipeIngredientDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .amount(entity.getAmount())
                .build();
    }

    public static RecipeIngredient toEntity(RecipeIngredientDto dto, Recipe recipe) {
        return RecipeIngredient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .amount(dto.getAmount())
                .recipe(recipe)
                .build();
    }
}
