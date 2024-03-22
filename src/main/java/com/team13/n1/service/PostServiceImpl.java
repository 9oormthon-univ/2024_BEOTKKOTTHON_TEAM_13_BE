package com.team13.n1.service;


import com.team13.n1.entity.Post;
import com.team13.n1.entity.User;
import com.team13.n1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Integer postId) {
        return postRepository.findById(postId).orElse(null);
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}