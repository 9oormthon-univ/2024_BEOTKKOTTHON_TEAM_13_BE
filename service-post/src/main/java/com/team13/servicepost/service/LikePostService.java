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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class LikePostService {

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Transactional
    public String toggleLikePost(Long postId, Long userId) {
        // 해당 Post 존재 여부 확인
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return "게시글 없음";  // 게시글이 없을 경우
        }

        // 좋아요 누른 사용자가 존재하는지 확인
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return "유저 없음";  // 사용자가 없을 경우
        }

        // 기존에 좋아요를 눌렀는지 확인
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            // 이미 좋아요를 눌렀다면 취소
            likePostRepository.delete(existingLike.get());
            return "좋아요 취소";
        } else {
            // 좋아요 추가
            LikePost likePost = new LikePost();
            likePost.setPost(postOptional.get());
            likePost.setUserId(userId);
            likePostRepository.save(likePost);
            return "좋아요 실행";
        }
    }


    public Long getLikesCount(Long postId) {
        return likePostRepository.countByPostId(postId);
    }

    // 특정 사용자가 좋아요한 게시글 ID 리스트를 반환하는 메서드
    public List<Long> getLikedPostIdsByUserId(Long userId) {
        List<LikePost> likedPosts = likePostRepository.findByUserId(userId);
        return likedPosts.stream()
                .map(likePost -> likePost.getPost().getId()) // 각 LikePost 엔티티에서 Post의 ID를 가져옴
                .collect(Collectors.toList());
    }
}
