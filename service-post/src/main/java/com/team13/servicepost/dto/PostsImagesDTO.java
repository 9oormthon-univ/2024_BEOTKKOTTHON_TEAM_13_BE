package com.team13.servicepost.dto;

import lombok.Data;

@Data
public class PostsImagesDTO {
    private Long id;
    private Long postId;
    private String imagePath;
}
