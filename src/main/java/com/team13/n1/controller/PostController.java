package com.team13.n1.controller;

import com.team13.n1.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.persistence.PostRemove;
import lombok.RequiredArgsConstructor;
import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.entity.User;
import com.team13.n1.repository.PostIngredientRepository;
import com.team13.n1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Log4j2
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostIngredientRepository postIngredientRepository;

    @GetMapping("/posts")
    public ResponseEntity<List<Map<String, Object>>> getPostsWithIngredients() {
        List<Post> posts = postRepository.findAll();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> postData = createPostData(post);
            data.add(postData);
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("/posts/{post_id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable("post_id") Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Post post = postOptional.get();
        Map<String, Object> postData = createPostData(post);
        return ResponseEntity.ok(postData);
    }

    private Map<String, Object> createPostData(Post post) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("postid", post.getId());
        postData.put("posttitle", post.getTitle());
        postData.put("postprice", post.getPrice());
        postData.put("created_at", post.getCreatedAt());
        postData.put("postImages", post.getImages());
        postData.put("postContents", post.getContents());
        postData.put("postGroupSize", post.getGroupSize());
        postData.put("postCurGroupSize", post.getCurGroupSize());
        postData.put("closedAt", post.getClosedAt());

        List<Map<String, Object>> ingredientData = new ArrayList<>();
        for (PostIngredient ingredient : post.getPostIngredients()) {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("ingreid", ingredient.getId());
            ingredientMap.put("ingrename", ingredient.getName());
            ingredientMap.put("ingreurl", ingredient.getUrl());
            ingredientData.add(ingredientMap);
        }
        postData.put("ingredients", ingredientData);

        User user = post.getUser();
        Map<String, Object> userData = new HashMap<>();
        userData.put("userid", user.getId());
        userData.put("usernickname", user.getNickname());
        userData.put("userrating", user.getRating());
        userData.put("userprofile", user.getProfile());
        postData.put("user", userData);

        return postData;
    }


}

