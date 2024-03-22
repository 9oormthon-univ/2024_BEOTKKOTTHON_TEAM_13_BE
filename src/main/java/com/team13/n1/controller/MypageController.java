package com.team13.n1.controller;


import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.entity.User;
import com.team13.n1.service.PostIngredientService;
import com.team13.n1.service.PostService;
import com.team13.n1.service.UserLikePostService;
import com.team13.n1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserLikePostService userLikePostService;

    @Autowired
    private PostIngredientService postIngredientService;

    @GetMapping("/{user_id}")
    public ResponseEntity<Map<String, Object>> getUserData(@PathVariable("user_id") String userId) {
        // 사용자 정보 가져오기
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // 사용자의 정보
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", user.getId());
        userData.put("nickname", user.getNickname());
        userData.put("rating", user.getRating());
        userData.put("profileImage", user.getProfile());

        // 사용자가 작성한 글 목록 가져오기
        List<Post> userPosts = postService.getPostsByUser(user);
        userData.put("userPosts", userPosts.stream().map(this::mapPostData).collect(Collectors.toList()));

        // 사용자가 좋아요한 글 목록 가져오기
        List<Post> userLikedPosts = userLikePostService.getLikedPostsByUser(user);
        userData.put("userLikedPosts", userLikedPosts.stream().map(this::mapPostData).collect(Collectors.toList()));

        return ResponseEntity.ok(userData);
    }

    // 게시물 정보를 맵으로 변환하는 메소드
    private Map<String, Object> mapPostData(Post post) {
        Map<String, Object> postData = new HashMap<>();
        postData.put("postId", post.getId());
        postData.put("title", post.getTitle());
        postData.put("price", post.getPrice());
        postData.put("images", post.getImages());

        // 게시물 재료 정보 가져오기
        List<PostIngredient> ingredients = postIngredientService.getIngredientsByPost(post);
        postData.put("ingredients", ingredients.stream().map(PostIngredient::getName).collect(Collectors.toList()));

        return postData;
    }
}
