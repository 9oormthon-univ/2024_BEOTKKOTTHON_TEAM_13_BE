package com.team13.servicepost.controller;

import com.team13.servicepost.service.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class LikePostController {

    private final LikePostService likePostService;

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        boolean success = likePostService.likePost(postId, userId);
        if (success) {
            return ResponseEntity.ok("Post liked successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have already liked this post, post not found, or user not found.");
        }
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Long> getLikesCount(@PathVariable Long postId) {
        Long likesCount = likePostService.getLikesCount(postId);
        return ResponseEntity.ok(likesCount);
    }
}