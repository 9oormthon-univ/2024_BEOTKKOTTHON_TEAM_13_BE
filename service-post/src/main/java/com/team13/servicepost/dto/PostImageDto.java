package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import lombok.Data;

@Data
public class PostImageDto {
    private Long id;
    private String imagePath;

    public static PostImageDto from(PostImage image) {
        PostImageDto dto = new PostImageDto();
        dto.setId(image.getId());
        dto.setImagePath(image.getImagePath());
        return dto;
    }

    public static PostImage toEntity(PostImageDto dto) {
        PostImage entity = new PostImage();
        entity.setId(dto.getId());
        entity.setImagePath(dto.getImagePath());
        return entity;
    }

    public static PostImage toEntity(PostImageDto dto, Post post) {
        PostImage entity = toEntity(dto);
        entity.setPost(post);
        return entity;
    }
}
