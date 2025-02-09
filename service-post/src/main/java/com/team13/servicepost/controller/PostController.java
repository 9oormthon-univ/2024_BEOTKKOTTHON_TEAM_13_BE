package com.team13.servicepost.controller;

import com.team13.servicepost.dto.MypagePostResponseDto;
import com.team13.servicepost.dto.PostRequestDto;
import com.team13.servicepost.dto.PostResponseDto;
import com.team13.servicepost.service.LikePostService;
import com.team13.servicepost.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private LikePostService likePostService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");

        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            // 게시글과 관련된 세부 정보 저장
            PostResponseDto savedPostResponse = postService.createPostWithDetails(postRequestDto, userId);
            return new ResponseEntity<>(savedPostResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // 오류 발생 시 400 BAD REQUEST 반환
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("postId") Long postId) {
        Optional<PostResponseDto> postWithDetails = postService.getPostWithUserDetails(postId);
        if (postWithDetails.isPresent()) {
            return ResponseEntity.ok(postWithDetails.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<String> toggleLikePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader);
            return likePostService.toggleLikePost(postId, userId)
                    ? ResponseEntity.ok("게시글에 대한 좋아요 혹은 좋아요 취소가 실행됐습니다")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글 혹은 유저확인이 문제로 좋아요 관련 기능이 실행되지 않았습니다.");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/like/{postId}")
    public ResponseEntity<Long> getLikesCount(@PathVariable("postId") Long postId) {
        Long likesCount = likePostService.getLikesCount(postId);
        return ResponseEntity.ok(likesCount);
    }

    @GetMapping("/user")
    public ResponseEntity<List<MypagePostResponseDto>> getPostsByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypagePostResponseDto> posts = postService.getPostsByUserId(userId);
            if (posts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(posts);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 사용자가 좋아요한 게시글 가져오기
    @GetMapping("/like/user")
    public ResponseEntity<List<MypagePostResponseDto>> getLikePostsByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypagePostResponseDto> likedPosts = postService.getLikePostsByUserId(userId);
            if (likedPosts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(likedPosts);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
