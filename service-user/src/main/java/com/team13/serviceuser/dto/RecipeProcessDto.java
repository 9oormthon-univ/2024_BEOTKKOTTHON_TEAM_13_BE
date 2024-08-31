package com.team13.serviceuser.dto;
import lombok.Data;
@Data
public class RecipeProcessDto {
    private Long id;
    private Long recipeId;
    private String imagePath;
    private String contents;
}