package com.team13.n1.controller;

import com.team13.n1.entity.UserSession;
import com.team13.n1.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
@Log4j2
@RequiredArgsConstructor
public class UserController {
    private final UserService service;
    private final UserSessionService sessService;
    private final UserLikesService likesService;
    private final PostService postService;
    private final RecipeService recipeService;

    @PostMapping("signup")
    public String signUp(@RequestBody Map<String, String> request) {
        service.addUser(request.get("user_id"), request.get("nickname"));
        return "ok";
    }

    @PostMapping("signin")
    public String signIn(@RequestBody Map<String, String> request) {
        if (service.existsById(request.get("user_id"))) {
            String sessionId = UUID.randomUUID().toString();
            sessService.save(new UserSession(sessionId, request.get("user_id")));
            return sessionId;
        }
        return "";
    }

    // 사용자 닉네임, 만족도 반환
    @GetMapping("info")
    public Map<String, Object> info(@RequestParam String session_id) {
        if (sessService.existsById(session_id)) {
            String userId = sessService.getUserIdBySessionId(session_id);
            return service.getUserInfo(userId);
        }
        return new HashMap<>();
    }

    // 사용자가 작성한 글
    @GetMapping("posts")
    public List<Map<String, Object>> userPosts(@RequestParam String session_id) {
        if (sessService.existsById(session_id)) {
            String userId = sessService.getUserIdBySessionId(session_id);
            return postService.getSimplePostsByUserId(userId);
        }
        return new ArrayList<>();
    }

    // 사용자가 작성한 레시피
    @GetMapping("recipes")
    public List<Map<String, Object>> userRecipes(@RequestParam String session_id) {
        if (sessService.existsById(session_id)) {
            String userId = sessService.getUserIdBySessionId(session_id);
            return recipeService.getSimpleRecipesByUserId(userId);
        }
        return new ArrayList<>();
    }

    // 사용자가 좋아요 한 공동구매 및 레시피
    @GetMapping("likes")
    public List<Map<String, Object>> userLikesPost(@RequestParam String session_id, @RequestParam String type) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (sessService.existsById(session_id)) {
            String userId = sessService.getUserIdBySessionId(session_id);
            if (type.equals("post"))
                result.addAll(likesService.getUserLikesPosts(userId));
            else if (type.equals("recipe"))
                result.addAll(likesService.getUserLikesRecipes(userId));
        }

        return result;
    }

    // 사용자 공동구매 혹은 레시피 좋아요
    @PostMapping("likes")
    public void likesPostRecipe(@RequestBody Map<String, String> request) {
        if (sessService.existsById(request.get("session_id"))) {
            if (request.get("type").equals("post")) {
                service.likesPost(Integer.parseInt(request.get("id")), sessService.getUserIdBySessionId(request.get("session_id")));
            } else if (request.get("type").equals("recipe")) {
                service.likesRecipe(Integer.parseInt(request.get("id")), sessService.getUserIdBySessionId(request.get("session_id")));
            }
        }
    }
}
