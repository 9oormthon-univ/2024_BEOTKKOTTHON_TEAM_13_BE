package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostsImagesDTO;
import com.team13.servicepost.entity.PostsImages;
import com.team13.servicepost.repository.PostsImagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsImagesService {

    @Autowired
    private PostsImagesRepository postsImagesRepository;

    public PostsImages saveImage(PostsImages image) {
        return postsImagesRepository.save(image);
    }

    public List<PostsImages> getImagesByPostId(Long postId) {
        return postsImagesRepository.findByPostId(postId);
    }

    public PostsImagesDTO convertToDto(PostsImages images) {
        PostsImagesDTO dto = new PostsImagesDTO();
        dto.setId(images.getId());
        dto.setPostId(images.getPost().getId());
        dto.setImagePath(images.getImagePath());
        return dto;
    }

    public PostsImages convertToEntity(PostsImagesDTO dto) {
        PostsImages images = new PostsImages();
        images.setId(dto.getId());
        images.setImagePath(dto.getImagePath());
        return images;
    }
}