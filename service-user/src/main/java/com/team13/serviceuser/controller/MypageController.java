package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.*;
import com.team13.serviceuser.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final MyPageService myPageService;

    @Autowired
    public MypageController(MyPageService myPageService) {
        this.myPageService = myPageService;
    }

    // 사용자 정보 가져오기
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto user = myPageService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 사용자가 작성한 공동구매게시글 가져오기
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<MypagePostResponseDto>> getPostsByUserId(@PathVariable Long userId) {
        List<MypagePostResponseDto> posts = myPageService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    //사용자가 작성한 레시피 가져오기
    @GetMapping("/user/{userId}/recipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(@PathVariable Long userId) {
        List<MypageRecipeResponseDto> recipes = myPageService.getRecipesByUserId(userId);
        return ResponseEntity.ok(recipes);
    }

    //사용자가 좋아요 누른 공동구매게시글
    @GetMapping("/user/{userId}/likePosts")
    public ResponseEntity<List<MypagePostResponseDto>> getLikePostsByUserId(@PathVariable Long userId) {
        List<MypagePostResponseDto> likedPosts = myPageService.getLikePostsByUserId(userId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(likedPosts);
        }
    }


    //사용자가 좋아요 누른 레시피
    @GetMapping("/user/{userId}/likeRecipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(@PathVariable Long userId) {
        List<MypageRecipeResponseDto> likedRecipes = myPageService.getLikeRecipesByUserId(userId);
        if (likedRecipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(likedRecipes);
        }
    }


}
