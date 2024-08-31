package com.team13.serviceuser.controller;

import com.team13.serviceuser.dto.PostResponseDto;
import com.team13.serviceuser.dto.UserDto;
import com.team13.serviceuser.service.MyPageService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // 사용자가 작성한 글 가져오기
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable Long userId) {
        List<PostResponseDto> posts = myPageService.getPostsByUserId(userId);
        return ResponseEntity.ok(posts);
    }



}
