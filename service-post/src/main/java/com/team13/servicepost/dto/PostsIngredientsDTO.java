package com.team13.servicepost.dto;

import lombok.Data;

@Data
public class PostsIngredientsDTO {
    private Long id;
    private Long postId;
    private String name;
    private String url;
}
