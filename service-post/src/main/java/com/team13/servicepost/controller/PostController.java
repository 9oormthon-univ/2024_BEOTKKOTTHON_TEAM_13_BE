package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostImageDTO;
import com.team13.servicepost.dto.PostWithUserDetails;
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

    @Autowired
    private PostImageService postImageService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        boolean userExists = postService.checkUserExists(post.getUserId());
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



    @PostMapping("/{postId}/images")
    public ResponseEntity<PostImage> addImage(@PathVariable Long postId, @RequestBody PostImage image) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            image.setPost(post.get());
            return new ResponseEntity<>(postImageService.saveImage(image), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{postId}/images")
//    public ResponseEntity<List<PostImage>> getImagesByPostId(@PathVariable Long postId) {
//        return new ResponseEntity<>(postImageService.getImagesByPostId(postId), HttpStatus.OK);
//    }

    @GetMapping("/{postId}/images")
    public ResponseEntity<List<PostImageDTO>> getImagesByPostId(@PathVariable Long postId) {
        List<PostImage> images = postImageService.getImagesByPostId(postId);
        List<PostImageDTO> imagesDTO = images.stream()
                .map(postImageService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(imagesDTO, HttpStatus.OK);
    }

}
