package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.UserDto;
import com.team13.servicerecipe.entity.LikeRecipe;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.LikeRecipeRepository;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeRecipeService {

    @Autowired
    private LikeRecipeRepository likeRecipeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Transactional
    public String toggleLikeRecipe(Long recipeId, Long userId) {
        // 레시피 존재 여부 확인
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            return "레시피 없음";  //  에러 처리를 위해 반환값 변경
        }

        // 사용자 존재 여부 확인
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return "유저 없음";  //  에러 처리를 위해 반환값 변경
        }

        // 기존 좋아요 여부 확인
        Optional<LikeRecipe> existingLike = likeRecipeRepository.findByRecipeIdAndUserId(recipeId, userId);
        if (existingLike.isPresent()) {
            likeRecipeRepository.delete(existingLike.get());
            return "좋아요 취소";
        } else {
            LikeRecipe likeRecipe = new LikeRecipe();
            likeRecipe.setRecipe(recipeOptional.get());
            likeRecipe.setUserId(userId);
            likeRecipeRepository.save(likeRecipe);
            return "좋아요 실행";
        }
    }

    public Long getLikesCount(Long recipeId) {
        return likeRecipeRepository.countByRecipeId(recipeId);
    }

    public List<Long> getLikedRecipeIdsByUserId(Long userId) {
        List<LikeRecipe> likedRecipes = likeRecipeRepository.findByUserId(userId);
        return likedRecipes.stream()
                .map(likeRecipe -> likeRecipe.getRecipe().getId())
                .collect(Collectors.toList());
    }
}
