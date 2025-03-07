package com.team13.servicerecipe.controller;


import com.team13.servicerecipe.apiPayload.ApiResponse;
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
import java.util.function.Supplier;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    @Autowired
    private RecipeService recipeService;

    @Autowired
    private LikeRecipeService likeRecipeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RecipeResponseDto>> createRecipe(@RequestBody RecipeRequestDto recipeRequestDto, @RequestHeader("X-User-Id") Long userId) {
        ApiResponse<RecipeResponseDto> response = recipeService.createRecipeWithDetails(recipeRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeResponseDto> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        return recipeService.getRecipeWithUserDetails(recipeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PostMapping("/like/{recipeId}")
    public ResponseEntity<String> toggleLikeRecipe(@PathVariable("recipeId") Long recipeId, @RequestHeader("X-User-Id") Long userId) {
            boolean success = likeRecipeService.toggleLikeRecipe(recipeId, userId);
            return success
                    ? ResponseEntity.ok("게시글에 대한 좋아요 혹은 좋아요 취소가 실행됐습니다")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("해당 게시글 혹은 유저확인이 문제로 좋아요 관련 기능이 실행되지 않았습니다.");
    }

    //단순 확인용 나중에 지울예정 postResponseDto에서 좋아요수까지 확인가능
    @GetMapping("/like/{recipeId}")
    public ResponseEntity<Long> getLikesCount(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity.ok(likeRecipeService.getLikesCount(recipeId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<MypageRecipeResponseDto>> getRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
            List<MypageRecipeResponseDto> recipes = recipeService.getRecipesByUserId(userId);
            return recipes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(recipes);
    }

    @GetMapping("/like/user")
    public ResponseEntity<List<MypageRecipeResponseDto>> getLikeRecipesByUserId(@RequestHeader("X-User-Id") Long userId) {
            List<MypageRecipeResponseDto> likedRecipes = recipeService.getLikeRecipesByUserId(userId);
            return likedRecipes.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
                    : ResponseEntity.ok(likedRecipes);
    }
}
