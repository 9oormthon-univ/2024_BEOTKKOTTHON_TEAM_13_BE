package com.team13.n1.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecipeCreateRequest {
    private String userId;
    private String title;
    private String thumbnailImage;
    private List<RecipeIngredientDto> ingredients;
    private List<RecipeProcessDto> processes;

}