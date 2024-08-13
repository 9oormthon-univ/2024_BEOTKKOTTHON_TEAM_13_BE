package com.team13.servicepost.controller;

import com.team13.servicepost.dto.PostWithUserDetails;
import com.team13.servicepost.dto.PostsImagesDTO;
import com.team13.servicepost.dto.PostsIngredientsDTO;
import com.team13.servicepost.entity.Post;
import com.team13.servicepost.entity.PostsImages;
import com.team13.servicepost.entity.PostsIngredients;
import com.team13.servicepost.service.PostService;
import com.team13.servicepost.service.PostsImagesService;
import com.team13.servicepost.service.PostsIngredientsService;
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
    private PostsIngredientsService postsIngredientsService;

    @Autowired
    private PostsImagesService postsImagesService;

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        boolean userExists = postService.checkUserExists(post.getUsersId());
        if (userExists) {
            return new ResponseEntity<>(postService.savePost(post), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<PostWithUserDetails> getPostById(@PathVariable Long id) {
//        return postService.getPostWithUserDetails(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
//    }

    @PostMapping("/{postId}/ingredients")
    public ResponseEntity<PostsIngredients> addIngredient(@PathVariable Long postId, @RequestBody PostsIngredients ingredient) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            ingredient.setPost(post.get());
            return new ResponseEntity<>(postsIngredientsService.saveIngredient(ingredient), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{postId}/ingredients")public ResponseEntity<List<PostsIngredientsDTO>> getIngredientsByPostId(@PathVariable Long postId) {
        List<PostsIngredients> ingredients = postsIngredientsService.getIngredientsByPostId(postId);
        List<PostsIngredientsDTO> ingredientsDTO = ingredients.stream()
                .map(postsIngredientsService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(ingredientsDTO, HttpStatus.OK);
    }

    @PostMapping("/{postId}/images")
    public ResponseEntity<PostsImages> addImage(@PathVariable Long postId, @RequestBody PostsImages image) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            image.setPost(post.get());
            return new ResponseEntity<>(postsImagesService.saveImage(image), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/{postId}/images")
//    public ResponseEntity<List<PostsImages>> getImagesByPostId(@PathVariable Long postId) {
//        return new ResponseEntity<>(postsImagesService.getImagesByPostId(postId), HttpStatus.OK);
//    }

    @GetMapping("/{postId}/images")
    public ResponseEntity<List<PostsImagesDTO>> getImagesByPostId(@PathVariable Long postId) {
        List<PostsImages> images = postsImagesService.getImagesByPostId(postId);
        List<PostsImagesDTO> imagesDTO = images.stream()
                .map(postsImagesService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(imagesDTO, HttpStatus.OK);
    }

}
