package com.team13.n1.controller;



import com.team13.n1.dto.PostDTO;
import com.team13.n1.entity.Post;
import com.team13.n1.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("list")
    public List<Map<String, String>> list() {
        List<Map<String, String>> result = new ArrayList<>();

        return new ArrayList<>();
    }

//    @GetMapping("/{postId}")
//    public PostDTO getPostById(@PathVariable("postId") Integer postId) {
//        return postService.getPostById(postId);
//    }

    @GetMapping("{postId}")
    public PostDTO getPost() {
        // 예시용 데이터로 객체 생성
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(1);
        postDTO.setUserId("soo24");
        postDTO.setChatId("joamksh");
        postDTO.setTitle("카레치즈라면");
        postDTO.setPostImages("/user-image/recipe/1/thumbnail.png");
        postDTO.setPostStatus(0);
        postDTO.setPrice("5000원");
        postDTO.setUserLike(5);
        postDTO.setUserNickname("수현");
        postDTO.setUserScore(79.3f);
        postDTO.setIngredientName("감자");
        postDTO.setIngredientUrl("www.coupang/");
        postDTO.setContents("감자 팔아요 5명 5kg나눠요");
        postDTO.setGroupSize(5);
        postDTO.setCurGroupSize(3);
        postDTO.setCreatedAt("2024년 3월 19일");

        return postDTO;
    }





}
