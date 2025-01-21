package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.*;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.repository.UserRepository;
import com.team13.serviceuser.service.MyPageService;
import com.team13.serviceuser.service.SignInService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/info")
    public ResponseEntity<UserDto> getUserInfo(HttpServletRequest request) {
        // Gateway에서 전달한 사용자 ID를 헤더에서 가져옴
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            UserDto user = myPageService.getUserById(userId); // 서비스 호출
            return ResponseEntity.ok(user);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 사용자가 작성한 공동구매게시글 가져오기
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<MypagePostResponseDto>> getPostsByUserId(@PathVariable("userId") Long userId) {
        List<MypagePostResponseDto> posts = myPageService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }

    //사용자가 작성한 레시피 가져오기
    @GetMapping("/recipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypageRecipeResponseDto> recipes = myPageService.getRecipesByUserId(userId);
            return ResponseEntity.ok(recipes);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //사용자가 좋아요 누른 공동구매게시글
    @GetMapping("/user/{userId}/likePosts")
    public ResponseEntity<List<MypagePostResponseDto>> getLikePostsByUserId(@PathVariable("userId") Long userId) {
        List<MypagePostResponseDto> likedPosts = myPageService.getLikePostsByUserId(userId);
        if (likedPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(likedPosts);
        }
    }

    //사용자가 좋아요 누른 레시피
    @GetMapping("/likeRecipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypageRecipeResponseDto> likedRecipes = myPageService.getLikeRecipesByUserId(userId);
            if (likedRecipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(likedRecipes);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        UserDto updatedUser = myPageService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }


}
