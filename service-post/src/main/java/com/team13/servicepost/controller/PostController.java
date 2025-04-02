package com.team13.servicepost.controller;

import com.team13.servicepost.apiPyaload.ApiResponse;
import com.team13.servicepost.apiPyaload.code.status.ErrorStatus;
import com.team13.servicepost.apiPyaload.code.status.SuccessStatus;

import com.team13.servicepost.dto.MyPostDto;
import com.team13.servicepost.dto.MyPostLikeDto;
import com.team13.servicepost.dto.MyPostListDto;
import com.team13.servicepost.dto.PostRequestDto;
import com.team13.servicepost.dto.PostResponseDto;
import com.team13.servicepost.service.LikePostService;
import com.team13.servicepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private LikePostService likePostService;

    @PostMapping
    public ApiResponse<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @RequestHeader("X-User-Id") Long userId) {
        PostResponseDto responseDto = postService.createPostWithDetails(postRequestDto, userId);
        return ApiResponse.of(SuccessStatus.POST_CREATED, responseDto);
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> getPostById(@PathVariable("postId") Long postId) {
        Optional<PostResponseDto> postOptional = postService.getPostWithUserDetails(postId);

        if (postOptional.isPresent()) {
            return ApiResponse.of(SuccessStatus.POST_FOUND, postOptional.get());
        }

        return ApiResponse.onFailure("POST_NOT_FOUND", "게시글을 찾을 수 없습니다.", null);
    }

    @PostMapping("/like/{postId}")
    public ApiResponse<String> toggleLike(@PathVariable Long postId, @RequestHeader("X-User-Id") Long userId) {
        String result = likePostService.toggleLikePost(postId, userId);

        if (result.equals("좋아요 실행")) {
            return ApiResponse.of(SuccessStatus.LIKE_ADDED, "좋아요가 추가되었습니다.");
        }

        if (result.equals("좋아요 취소")) {
            return ApiResponse.of(SuccessStatus.LIKE_REMOVED, "좋아요가 취소되었습니다.");
        }

        if (result.equals("게시글 없음")) {
            return ApiResponse.onFailure("POST_NOT_FOUND", "게시글을 찾을 수 없습니다.", null);
        }

        if (result.equals("유저 없음")) {
            return ApiResponse.onFailure("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.", null);
        }

        return ApiResponse.onFailure("LIKE_FAILED", "좋아요 처리에 실패했습니다.", null);
    }


    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/like/{postId}")
    public ResponseEntity<Long> getLikesCount(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(likePostService.getLikesCount(postId));
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<MyPostListDto<MyPostDto>>> getPostsByUserId(
            @RequestHeader("X-User-Id") Long userId) {

        MyPostListDto<MyPostDto> posts = postService.getPostsByUserId(userId);

        return posts.getPosts().isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(
                ErrorStatus.EMPTY_DATA.getCode(),
                ErrorStatus.EMPTY_DATA.getMessage(),
                null))
                : ResponseEntity.ok(ApiResponse.onSuccess(posts));
    }

    // 사용자가 좋아요한 게시글 가져오기
    @GetMapping("/like/user")
    public ResponseEntity<ApiResponse<MyPostListDto<MyPostLikeDto>>> getLikePostsByUserId(
            @RequestHeader("X-User-Id") Long userId,@RequestParam("type") int type) {

        MyPostListDto<MyPostLikeDto> likedPosts = postService.getLikePostsResponseByUserId(userId, type);

        return likedPosts.getPosts().isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.onFailure(
                        ErrorStatus.EMPTY_DATA.getCode(),
                        ErrorStatus.EMPTY_DATA.getMessage(),
                        null))
                : ResponseEntity.ok(ApiResponse.onSuccess(likedPosts));
    }

}
