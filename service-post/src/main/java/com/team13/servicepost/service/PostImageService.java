package com.team13.servicepost.service;

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

}
