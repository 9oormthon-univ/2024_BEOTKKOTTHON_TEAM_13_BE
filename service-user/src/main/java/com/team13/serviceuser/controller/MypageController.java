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
    public ResponseEntity<MypagePostListResponseDto> getPostsByUserId(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(myPageService.getPostsByUserId(userId));
    }

    //사용자가 작성한 레시피 가져오기
    @GetMapping("/recipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(myPageService.getRecipesByUserId(userId));
    }

    //사용자가 좋아요 누른 공동구매게시글
//    @GetMapping("/likePosts")
//    public ResponseEntity<List<MypagePostResponseDto>> getLikePostsByUserId(@RequestHeader("X-User-Id") Long userId) {
//            List<MypagePostResponseDto> likedPosts = myPageService.getLikePostsByUserId(userId);
//            return likedPosts.isEmpty()
//                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
//                    : ResponseEntity.ok(likedPosts);
//    }

    //사용자가 좋아요 누른 레시피
    @GetMapping("/likeRecipes")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
            List<MypageRecipeResponseDto> likedRecipes = myPageService.getLikeRecipesByUserId(userId);
            return likedRecipes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(likedRecipes);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(myPageService.updateUser(userId, userDto));
    }

}
