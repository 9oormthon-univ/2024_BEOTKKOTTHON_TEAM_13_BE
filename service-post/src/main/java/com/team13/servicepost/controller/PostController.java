package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.dto.PostResponseDto;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.service.PostImageService;
import com.team13.servicepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

//    @PostMapping
//    public ResponseEntity<Post> createPost(@RequestBody Post post) {
//        boolean userExists = postService.checkUserExists(post.getUserId());
//        if (userExists) {
//            return new ResponseEntity<>(postService.savePost(post), HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Optional<PostResponseDto> postWithDetails = postService.getPostWithUserDetails(id);
        if (postWithDetails.isPresent()) {
            return ResponseEntity.ok(postWithDetails.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
