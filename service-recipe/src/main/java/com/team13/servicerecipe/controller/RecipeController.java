package com.team13.servicerecipe.controller;


import com.team13.servicerecipe.dto.MypageRecipeResponseDto;
import com.team13.servicerecipe.dto.RecipeRequestDto;
import com.team13.servicerecipe.dto.RecipeResponseDto;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.service.LikeRecipeService;
import com.team13.servicerecipe.service.RecipeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private LikeRecipeService likeRecipeService;

    @PostMapping
    public ResponseEntity<RecipeResponseDto> createRecipe(@RequestBody RecipeRequestDto recipeRequestDto, HttpServletRequest request) {
        // Gateway에서 전달한 사용자 ID를 헤더에서 가져옴
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            RecipeResponseDto savedRecipeResponse = recipeService.createRecipeWithDetails(recipeRequestDto, userId);
            return new ResponseEntity<>(savedRecipeResponse, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            // 헤더에서 가져온 사용자 ID가 숫자로 변환되지 않을 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            // 비즈니스 로직에서 발생하는 기타 예외 처리
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        Optional<RecipeResponseDto> recipeWithDetails = recipeService.getRecipeWithUserDetails(recipeId);
        if (recipeWithDetails.isPresent()) {
            return ResponseEntity.ok(recipeWithDetails.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // localhost:4328:1/like?userId=2
    //userId가 2인 사람이 Recipe1번 글 좋아요를 누른다.
    @PostMapping("/{recipeId}/like")
    public ResponseEntity<String> toggleLikeRecipe(@PathVariable("recipeId") Long recipeId, HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            boolean success = likeRecipeService.toggleLikeRecipe(recipeId, userId);
            if (success) {
                return ResponseEntity.ok("게시글에 대한 좋아요 혹은 좋아요 취소가 실행됐습니다");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글 혹은 유저확인이 문제로 좋아요 관련 기능이 실행되지 않았습니다.");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/{recipeId}/likes")
    public ResponseEntity<Long> getLikesCount(@PathVariable("recipeId") Long recipeId) {
        Long likesCount = likeRecipeService.getLikesCount(recipeId);
        return ResponseEntity.ok(likesCount);
    }

    @GetMapping("/user")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypageRecipeResponseDto> recipes = recipeService.getRecipesByUserId(userId);
            if (recipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(recipes);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/like/user")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");

        if (userIdHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        try {
            Long userId = Long.valueOf(userIdHeader); // 사용자 ID 파싱
            List<MypageRecipeResponseDto> likedRecipes = recipeService.getLikeRecipesByUserId(userId);
            if (likedRecipes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } else {
                return ResponseEntity.ok(likedRecipes);
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}