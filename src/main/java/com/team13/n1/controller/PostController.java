package com.team13.n1.controller;

import com.team13.n1.dto.PostCreateRequest;
import com.team13.n1.service.PostCreateService;
import com.team13.n1.service.PostService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Log4j2
@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService service;
    private final PostCreateService postCreateService;

    @Autowired
    public PostController(PostService service, PostCreateService postCreateService) {
        this.service = service;
        this.postCreateService = postCreateService;
    }

    @GetMapping("list")
    public List<Map<String, Object>> list(@RequestParam("bcode") String bCode,
                                          @RequestParam("type") String type,
                                          @RequestParam("keyword") String keyword,
                                          @RequestParam("page") String page) {
        return service.getList(bCode, type, keyword, page);
    }

    @GetMapping("{post_id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable("post_id") int postId) {
        return service.getPost(postId);
    }



    @PostMapping("/r_ingd")
    public ResponseEntity<String> postRIngd(@RequestBody PostCreateRequest request) {
        postCreateService.postRIngd(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
    }


@PostMapping("/r_ingd/image")
public ResponseEntity<String> postRIngdImage(@RequestParam("imageFile") MultipartFile imageFile) {
    String imagePath = postCreateService.saveImageAndReturnPath(imageFile);
    return ResponseEntity.status(HttpStatus.CREATED).body(imagePath);
}






}
