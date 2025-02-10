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
import java.util.function.Supplier;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private LikePostService likePostService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto,@RequestHeader("X-User-Id") Long userId) {
        return handleException(() -> ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPostWithDetails(postRequestDto, userId)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable("postId") Long postId) {
        return handleException(() -> postService.getPostWithUserDetails(postId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)));
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<String> toggleLikePost(@PathVariable("postId") Long postId, @RequestHeader("X-User-Id") Long userId) {
        return handleException(() -> likePostService.toggleLikePost(postId, userId)
                ? ResponseEntity.ok("게시글에 대한 좋아요 혹은 좋아요 취소가 실행됐습니다")
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글 혹은 유저확인이 문제로 좋아요 관련 기능이 실행되지 않았습니다."));
    }

    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/like/{postId}")
    public ResponseEntity<Long> getLikesCount(@PathVariable("postId") Long postId) {
        return handleException(() -> ResponseEntity.ok(likePostService.getLikesCount(postId)));
    }

    @GetMapping("/user")
    public ResponseEntity<List<MypagePostResponseDto>> getPostsByUserId( @RequestHeader("X-User-Id") Long userId) {
        return handleException(() -> {
            List<MypagePostResponseDto> posts = postService.getPostsByUserId(userId);
            return posts.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(posts);
        });
    }

    // 사용자가 좋아요한 게시글 가져오기
    @GetMapping("/like/user")
    public ResponseEntity<List<MypagePostResponseDto>> getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return handleException(() -> {
            List<MypagePostResponseDto> likedPosts = postService.getLikePostsByUserId(userId);
            return likedPosts.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(likedPosts);
        });
    }

    private <T> ResponseEntity<T> handleException(Supplier<ResponseEntity<T>> action) {
        try {
            return action.get();
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
