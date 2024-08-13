package com.team13.servicerecipe.dto;

import lombok.Data;

@Data
public class RecipesIngredientsDTO {
    private Long id;
    private Long recipeId;
    private String name;
    private String amount;
}
