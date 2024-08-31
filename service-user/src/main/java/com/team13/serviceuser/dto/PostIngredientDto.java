package com.team13.serviceuser.dto;

import lombok.Data;

@Data
public class PostIngredientDto {
    private Long id;
    private Long postId;
    private String name;
    private String url;
}
