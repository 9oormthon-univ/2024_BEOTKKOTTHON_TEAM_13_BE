package com.team13.servicerecipe.service;

import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.dto.RecipeWithUserDetails;
import com.team13.servicerecipe.dto.User;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService {


    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }


    public  boolean checkUserExists(Long userId) {
        ResponseEntity<User> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() ==  HttpStatus.OK;
    }

    public Optional<RecipeWithUserDetails> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            return Optional.empty();
        }

        Recipe recipe = recipeOptional.get();
        ResponseEntity<User> response = userServiceClient.getUserById(recipe.getUserId());
        if (response.getStatusCode() != HttpStatus.OK) {
            return Optional.empty();
        }

        User user = response.getBody();
        if (user == null) {
            return Optional.empty();
        }

        RecipeWithUserDetails recipeWithUserDetails = new RecipeWithUserDetails();
        recipeWithUserDetails.setId(recipe.getId());
        recipeWithUserDetails.setUserId(recipe.getUserId());
        recipeWithUserDetails.setTitle(recipe.getTitle());
        recipeWithUserDetails.setContents(recipe.getContents());
        recipeWithUserDetails.setCommentCount(recipe.getCommentCount());
        recipeWithUserDetails.setLikesCount(recipe.getLikesCount());
        recipeWithUserDetails.setThumbnailImagePath(recipe.getThumbnailImagePath());
        recipeWithUserDetails.setCreatedAt(recipe.getCreatedAt());
        recipeWithUserDetails.setType(recipe.getType());
        recipeWithUserDetails.setUserNickname(user.getNickname());

        return Optional.of(recipeWithUserDetails);
    }
}
