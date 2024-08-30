package com.team13.servicerecipe.dto;


import lombok.Data;

@Data
public class RecipeProcessDTO {
    private Long id;
    private Long recipeId;
    private String imagePath;
    private String contents;
}