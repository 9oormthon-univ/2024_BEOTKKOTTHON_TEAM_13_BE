package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostWithUserDetails;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        boolean userExists = postService.checkUserExists(post.getUsersId());
        if (userExists) {
            return new ResponseEntity<>(postService.savePost(post), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
//        return postService.getPostById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<PostWithUserDetails> getPostById(@PathVariable Long id) {
        return postService.getPostWithUserDetails(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

}