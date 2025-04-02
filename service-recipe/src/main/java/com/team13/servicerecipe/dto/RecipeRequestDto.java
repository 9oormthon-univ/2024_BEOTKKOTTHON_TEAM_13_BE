package com.team13.servicerecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeRequestDto {
    private String title;
    private String contents;
    private String thumbnailImagePath;
    private List<RecipeIngredientDto> ingredients;
    private List<RecipeProcessDto> processes;
}
