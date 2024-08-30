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

import java.util.Optional;

@Service
public class LikeRecipeService {

    @Autowired
    private LikeRecipeRepository likeRecipeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;


    @Transactional
    public boolean likeRecipe(Long recipeId, Long userId) {
        // Check if the recipe exists
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (recipeOptional.isEmpty()) {
            return false; // recipe does not exist
        }

        // Check if the user exists using Feign client
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return false; // User does not exist or UserService call failed
        }

        // Check if the user has already liked the recipe
        Optional<LikeRecipe> existingLike = likeRecipeRepository.findByRecipeIdAndUserId(recipeId, userId);
        if (existingLike.isPresent()) {
            return false; // User has already liked the recipe, do not allow another like
        }

        // Create a new Likerecipe entry
        LikeRecipe likeRecipe = new LikeRecipe();
        likeRecipe.setRecipe(recipeOptional.get());
        likeRecipe.setUserId(userId);

        // Save the like
        likeRecipeRepository.save(likeRecipe);
        return true; // Successfully liked the recipe
    }


    @Transactional
    public boolean toggleLikeRecipe(Long recipeId, Long userId) {
        //해당 recipe가 있는지 확인
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if(recipeOptional.isEmpty()){
            return false;
        }

        //좋아요 누르는 유저가 존재하는지 확인
        ResponseEntity<UserDto> userResponse = userServiceClient.getUserById(userId);
        if (userResponse.getStatusCode() != HttpStatus.OK || userResponse.getBody() == null) {
            return false;
        }

        Optional<LikeRecipe> existingLike = likeRecipeRepository.findByRecipeIdAndUserId(recipeId,userId);
        if (existingLike.isPresent()) {
            //이전에 좋아요 기록있다면 좋아요 취소 기능
            likeRecipeRepository.delete(existingLike.get());
            return true; //좋아요 취소 성공
        } else {
            // 이전 좋아요 없다면 좋아요 추가 기능
            LikeRecipe likeRecipe = new LikeRecipe();
            likeRecipe.setRecipe(recipeOptional.get());
            likeRecipe.setUserId(userId);
            likeRecipeRepository.save(likeRecipe);
            return true;// 좋아요 추가 성공
        }

    }

    public Long getLikesCount(Long recipeId) {
        return likeRecipeRepository.countByRecipeId(recipeId);
    }
}
