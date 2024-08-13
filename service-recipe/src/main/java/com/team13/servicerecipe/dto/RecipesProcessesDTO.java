package com.team13.servicerecipe.dto;

import lombok.Data;

@Data
public class RecipesProcessesDTO {
    private Long id;
    private Long recipeId;
    private String imagePath;
    private String contents;
}
