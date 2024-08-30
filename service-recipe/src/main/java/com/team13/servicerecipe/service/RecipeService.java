package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeIngredientDto;
import com.team13.servicerecipe.dto.RecipeResponseDto;
import com.team13.servicerecipe.dto.UserDto;
import com.team13.servicerecipe.entity.Recipe;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.feign.UserServiceClient;
import com.team13.servicerecipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeService {


    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private RecipeIngredientService recipeIngredientService;

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }


    public  boolean checkUserExists(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        return response.getStatusCode() ==  HttpStatus.OK;
    }

    public Optional<RecipeResponseDto> getRecipeWithUserDetails(Long recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if (!recipeOptional.isPresent()) {
            return Optional.empty();
        }

        Recipe recipe = recipeOptional.get();
        String userNickname = fetchUserNickname(recipe.getUserId());


        return Optional.of(buildRecipeResponseDto(recipe, userNickname));
    }

    private String fetchUserNickname(Long userId) {
        ResponseEntity<UserDto> response = userServiceClient.getUserById(userId);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch user nickname.");
        }
        return response.getBody().getNickname();
    }




    private RecipeResponseDto buildRecipeResponseDto(Recipe recipe, String userNickname) {
        List<RecipeIngredient> ingredients = recipeIngredientService.getIngredientsByRecipeId(recipe.getId());
        List<RecipeIngredientDto> ingredientDto = ingredients.stream()
                .map(recipeIngredientService::convertToDto)
                .collect(Collectors.toList());

        RecipeResponseDto recipeResponseDto = new RecipeResponseDto();
        recipeResponseDto.setId(recipe.getId());
        recipeResponseDto.setUserId(recipe.getUserId());
        recipeResponseDto.setTitle(recipe.getTitle());
        recipeResponseDto.setContents(recipe.getContents());
        recipeResponseDto.setCommentCount(recipe.getCommentCount());
        recipeResponseDto.setLikesCount(recipe.getLikesCount());
        recipeResponseDto.setThumbnailImagePath(recipe.getThumbnailImagePath());
        recipeResponseDto.setCreatedAt(recipe.getCreatedAt());
        recipeResponseDto.setType(recipe.getType());
        recipeResponseDto.setUserNickname(userNickname);
        recipeResponseDto.setIngredients(ingredientDto);

        return recipeResponseDto;
    }

}
