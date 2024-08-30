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
        //해당 Post가 있는지 확인
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return false;
        }

        //좋아요 누르는 유저가 존재하는지 확인
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return false;
        }

        //이미 예전에 좋아요 눌렀는지 확인
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            //이전에 좋아요 기록있다면 좋아요 취소 기능
            likePostRepository.delete(existingLike.get());
            return true; // 좋아요 취소 성공
        } else {
            // 이전 좋아요 없다면 좋아요 추가 기능
            LikePost likePost = new LikePost();
            likePost.setPost(postOptional.get());
            likePost.setUserId(userId);
            likePostRepository.save(likePost);
            return true; // 좋아요 추가 성공
        }
    }

    public Long getLikesCount(Long postId) {
        return likePostRepository.countByPostId(postId);
    }
}