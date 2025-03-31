package com.team13.serviceuser.controller;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.apiPyaload.code.status.ErrorStatus;
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
import java.util.function.Supplier;

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
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(myPageService.getUserById(userId));
    }

    // 사용자가 작성한 공동구매게시글 가져오기
    @GetMapping("/posts")
    public ResponseEntity<MypagePostListResponseDto<MyPostResponseDto>> getPostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(myPageService.getPostsByUserId(userId));
    }

    //사용자가 작성한 레시피 가져오기
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<MypageRecipeListResponseDto>> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
        MypageRecipeListResponseDto recipes = myPageService.getRecipesByUserId(userId);
        if (recipes.getRecipes().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(recipes));
    }

    //사용자가 좋아요 누른 공동구매게시글
    @GetMapping("/likePosts")
    public ResponseEntity<ApiResponse<MypagePostListResponseDto<MypagePostResponseDto>>>getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        MypagePostListResponseDto<MypagePostResponseDto> likedPosts = myPageService.getLikePostsByUserId(userId);
        if (likedPosts.getPosts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(likedPosts));
    }

    //사용자가 좋아요 누른 레시피
    @GetMapping("/likeRecipes")
    public ResponseEntity<ApiResponse<MypageRecipeListResponseDto>> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
        MypageRecipeListResponseDto likedRecipes = myPageService.getLikeRecipesByUserId(userId);
        if (likedRecipes.getRecipes().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null));
        }
        return ResponseEntity.ok(ApiResponse.onSuccess(likedRecipes));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(myPageService.updateUser(userId, userDto));
    }

}
