package com.team13.servicerecipe.dto;



import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class RecipeProcessDto {
    private Long id;
    private String imagePath;
    private String contents;
}