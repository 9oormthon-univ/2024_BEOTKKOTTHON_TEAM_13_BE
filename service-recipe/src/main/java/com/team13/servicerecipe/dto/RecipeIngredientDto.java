package com.team13.servicerecipe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeIngredientDto {
    private Long id;
    private String name;
    private String amount;
}