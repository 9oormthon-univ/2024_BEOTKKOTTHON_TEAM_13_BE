package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostIngredient;
import lombok.Data;

@Data
public class PostIngredientDto {
    private Long id;
    private String name;
    private String url;

    public static PostIngredientDto from(PostIngredient entity) {
        PostIngredientDto dto = new PostIngredientDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUrl(entity.getUrl());
        return dto;
    }

    public static PostIngredient toEntity(PostIngredientDto dto) {
        PostIngredient entity = new PostIngredient();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUrl(dto.getUrl());
        return entity;
    }

    public static PostIngredient toEntity(PostIngredientDto dto, Post post) {
        PostIngredient entity = toEntity(dto);
        entity.setPost(post);
        return entity;
    }
}
