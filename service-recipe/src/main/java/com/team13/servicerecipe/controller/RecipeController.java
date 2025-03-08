package com.team13.servicerecipe.controller;


import com.team13.servicerecipe.apiPayload.ApiResponse;
import com.team13.servicerecipe.apiPayload.code.status.ErrorStatus;
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
    public ResponseEntity<ApiResponse<RecipeResponseDto>> getRecipeById(@PathVariable("recipeId") Long recipeId) {
        ApiResponse<RecipeResponseDto> response = recipeService.getRecipeWithUserDetails(recipeId);
        if (!response.getIsSuccess()) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/like/{recipeId}")
    public ResponseEntity<ApiResponse<String>> toggleLikeRecipe(@PathVariable("recipeId") Long recipeId,
                                                                @RequestHeader("X-User-Id") Long userId) {
        String result = likeRecipeService.toggleLikeRecipe(recipeId, userId);

        //  응답 분기 처리
        if ("레시피 없음".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.onFailure(ErrorStatus.RECIPE_NOT_FOUND.getCode(),
                            ErrorStatus.RECIPE_NOT_FOUND.getMessage(),
                            null));
        }
        if ("유저 없음".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.onFailure(ErrorStatus.MEMBER_NOT_FOUND.getCode(),
                            ErrorStatus.MEMBER_NOT_FOUND.getMessage(),
                            null));
        }
        if ("좋아요 실행".equals(result)) {
            return ResponseEntity.ok(ApiResponse.onSuccess("좋아요를 실행했습니다."));
        }
        if ("좋아요 취소".equals(result)) {
            return ResponseEntity.ok(ApiResponse.onSuccess("좋아요 취소가 실행됐습니다."));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR.getCode(),
                        ErrorStatus._INTERNAL_SERVER_ERROR.getMessage(),
                        null));
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
