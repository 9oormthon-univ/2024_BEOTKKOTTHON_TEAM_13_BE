package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDTO;
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

    public PostImageDTO convertToDto(PostImage images) {
        PostImageDTO dto = new PostImageDTO();
        dto.setId(images.getId());
        dto.setPostId(images.getPost().getId());
        dto.setImagePath(images.getImagePath());
        return dto;
    }

    public PostImage convertToEntity(PostImageDTO dto) {
        PostImage images = new PostImage();
        images.setId(dto.getId());
        images.setImagePath(dto.getImagePath());
        return images;
    }
}