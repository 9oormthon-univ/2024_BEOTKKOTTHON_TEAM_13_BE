package com.team13.servicepost.service;

import com.team13.servicepost.dto.UserDto;
import com.team13.servicepost.entity.LikePost;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.feign.UserServiceClient;
import com.team13.servicepost.repository.LikePostRepository;
import com.team13.servicepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class LikePostService {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Transactional
    public boolean toggleLikePost(Long postId, Long userId) {
        // Check if the post exists
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false; // Post does not exist
        }

        // Check if the user exists using Feign client
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return false; // User does not exist or UserService call failed
        }

        // Check if the user has already liked the post
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            // If like exists, unlike the post
            likePostRepository.delete(existingLike.get());
            return true; // Successfully unliked the post
        } else {
            // If like does not exist, like the post
            LikePost likePost = new LikePost();
            likePost.setPost(postOptional.get());
            likePost.setUserId(userId);
            likePostRepository.save(likePost);
            return true; // Successfully liked the post
        }
    }

    public Long getLikesCount(Long postId) {
        return likePostRepository.countByPostId(postId);
    }
}