package com.team13.n1.service;

import com.team13.n1.dto.PostDTO;
import com.team13.n1.entity.Post;
import com.team13.n1.repository.PostRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDTO getPostById(Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            PostDTO postDTO = new PostDTO();
            BeanUtils.copyProperties(post, postDTO);
            return postDTO;
        }
        return null;
    }
}

