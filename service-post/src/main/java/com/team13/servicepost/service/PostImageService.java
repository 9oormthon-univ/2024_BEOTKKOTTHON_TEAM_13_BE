package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.repository.PostImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostImageService {

    @Autowired
    private PostImageRepository postImageRepository;

    public PostImage saveImage(PostImage image) {
        return postImageRepository.save(image);
    }

    public List<PostImage> getImagesByPostId(Long postId) {
        return postImageRepository.findByPostId(postId);
    }

    public PostImageDto convertToDto(PostImage images) {
        PostImageDto dto = new PostImageDto();
        dto.setId(images.getId());
        dto.setPostId(images.getPost().getId());
        dto.setImagePath(images.getImagePath());
        return dto;
    }

    public PostImage convertToEntity(PostImageDto dto) {
        PostImage images = new PostImage();
        images.setId(dto.getId());
        images.setImagePath(dto.getImagePath());
        return images;
    }
}