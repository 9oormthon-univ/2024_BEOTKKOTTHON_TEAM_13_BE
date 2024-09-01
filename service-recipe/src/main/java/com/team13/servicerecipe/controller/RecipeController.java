package com.team13.servicerecipe.controller;


import com.team13.servicerecipe.dto.MypageRecipeResponseDto;
import com.team13.servicerecipe.dto.RecipeRequestDto;
import com.team13.servicerecipe.dto.RecipeResponseDto;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.service.LikeRecipeService;
import com.team13.servicerecipe.service.RecipeService;
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
    public ResponseEntity<RecipeResponseDto> createRecipe(
            @RequestBody RecipeRequestDto recipeRequestDto,
            @RequestParam Long userId
    ) {
        try {
            RecipeResponseDto savedRecipeResponse = recipeService.createRecipeWithDetails(recipeRequestDto, userId);
            return new ResponseEntity<>(savedRecipeResponse, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable("id") Long id) {
        Optional<RecipeResponseDto> recipeWithDetails = recipeService.getRecipeWithUserDetails(id);
        if (recipeWithDetails.isPresent()) {
            return ResponseEntity.ok(recipeWithDetails.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // localhost:4328:1/like?userId=2
    //userId가 2인 사람이 Recipe1번 글 좋아요를 누른다.
    @PostMapping("/{recipeId}/like")
    public ResponseEntity<String> toggleLikeRecipe(@PathVariable("recipeId") Long recipeId, @RequestParam Long userId) {
        boolean success = likeRecipeService.toggleLikeRecipe(recipeId, userId);
        if (success) {
            return ResponseEntity.ok("게시글에 대한 좋아요 혹은 좋아요 취소가 실행됐습니다");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글 혹은 유저확인이 문제로 좋아요 관련 기능이 실행되지 않았습니다.");
        }
    }


    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/{recipeId}/likes")
    public ResponseEntity<Long> getLikesCount(@PathVariable("recipeId") Long recipeId) {
        Long likesCount = likeRecipeService.getLikesCount(recipeId);
        return ResponseEntity.ok(likesCount);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(@PathVariable("userId") Long userId) {
        List<MypageRecipeResponseDto> recipes = recipeService.getRecipesByUserId(userId);
        if (recipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(recipes);
        }
    }

    @GetMapping("/like/user/{userId}")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(@PathVariable("userId") Long userId) {
        List<MypageRecipeResponseDto> likedRecipes = recipeService.getLikeRecipesByUserId(userId);
        if (likedRecipes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(likedRecipes);
        }
    }


}