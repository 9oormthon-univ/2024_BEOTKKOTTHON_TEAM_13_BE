package com.team13.servicepost.dto;

import lombok.Data;

@Data
public class PostImageDto {
    private Long id;
    private Long postId;
    private String imagePath;
}
