package com.team13.n1.controller;

import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.team13.n1.entity.Post;
import com.team13.n1.entity.User;
import com.team13.n1.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getPostsWithIngredients() {
        List<Post> posts = postRepository.findAll();
        List<Map<String, Object>> data = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> postData = createPostData(post);
            data.add(postData);
        }
        return ResponseEntity.ok(data);
    }

    @GetMapping("{post_id}")
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
        postData.put("id", post.getId());
        postData.put("title", post.getTitle());
        postData.put("type", post.getType());
        postData.put("status", post.getStatus());
        postData.put("price", post.getPrice());
        postData.put("images", post.getImages());
        postData.put("contents", post.getContents());
        postData.put("group_size", post.getGroupSize());
        postData.put("cur_group_size", post.getCurGroupSize());
        postData.put("url", post.getUrl());
        postData.put("chat_id", post.getChatId());
        postData.put("closed_at", post.getClosedAt());
        postData.put("created_at", post.getCreatedAt());

        postData.put("location_bcode", post.getLocationBcode());
        postData.put("location_longitude", post.getLocationLongitude());
        postData.put("location_address", post.getLocationAddress());
        postData.put("location_latitude", post.getLocationLatitude());

        List<Map<String, Object>> ingredientData = new ArrayList<>();
        for (PostIngredient ingredient : post.getIngredients()) {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("name", ingredient.getName());
            ingredientMap.put("url", ingredient.getUrl());
            ingredientData.add(ingredientMap);
        }
        postData.put("ingredients", ingredientData);

        Map<String, Object> userData = new HashMap<>();
        Optional<User> user = userRepository.findById(post.getUserId());
        if (user.isPresent()) {
            userData.put("nickname", user.get().getNickname());
            userData.put("user_rating", user.get().getUserRating());
            userData.put("profile_image", user.get().getProfileImage());
            postData.put("user", userData);
        }

        return postData;
    }


}

