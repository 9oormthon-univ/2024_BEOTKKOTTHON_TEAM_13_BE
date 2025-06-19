package com.team13.serviceuser.controller;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.apiPyaload.code.status.ErrorStatus;
import com.team13.serviceuser.dto.MyPostDto;
import com.team13.serviceuser.dto.MyPostLikeDto;
import com.team13.serviceuser.dto.MyPostListDto;
import com.team13.serviceuser.dto.MyRecipeListDto;
import com.team13.serviceuser.dto.UserDto;
import com.team13.serviceuser.service.MyPageService;
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
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(myPageService.getUserById(userId));
    }

    // 사용자가 작성한 공동구매게시글 가져오기
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<MyPostListDto<MyPostDto>>> getPostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return myPageService.getPostsByUserId(userId)
                .map(posts -> ResponseEntity.ok(ApiResponse.onSuccess(posts)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), MyPostListDto.<MyPostDto>builder().build())));

    }

    //사용자가 작성한 레시피 가져오기
    @GetMapping("/recipes")
    public ResponseEntity<ApiResponse<MyRecipeListDto>> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
        return myPageService.getRecipesByUserId(userId)
                .map(recipes -> ResponseEntity.ok(ApiResponse.onSuccess(recipes)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null)));
    }

    //사용자가 좋아요 누른 공동구매게시글
    @GetMapping("/likePosts")
    public ResponseEntity<ApiResponse<MyPostListDto<MyPostLikeDto>>>getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return myPageService.getLikePostsByUserId(userId)
                .map(posts -> ResponseEntity.ok(ApiResponse.onSuccess(posts)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null)));
    }

    //사용자가 좋아요 누른 레시피
    @GetMapping("/likeRecipes")
    public ResponseEntity<ApiResponse<MyRecipeListDto>> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
        return myPageService.getLikeRecipesByUserId(userId)
                .map(recipes -> ResponseEntity.ok(ApiResponse.onSuccess(recipes)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.OK)
                        .body(ApiResponse.onFailure(ErrorStatus.DATA_EMPTY.getCode(), ErrorStatus.DATA_EMPTY.getMessage(), null)));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(myPageService.updateUser(userId, userDto));
    }
}
