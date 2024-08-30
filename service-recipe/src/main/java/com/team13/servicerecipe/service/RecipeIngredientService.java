package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeIngredientDTO;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository ;

    public RecipeIngredient saveIngredients(RecipeIngredient ingredient) {
        return recipeIngredientRepository .save(ingredient);
    }

    public List< RecipeIngredient> getIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientRepository .findByRecipeId(recipeId);
    }
    public  RecipeIngredientDTO convertToDto( RecipeIngredient ingredient) {
         RecipeIngredientDTO dto = new  RecipeIngredientDTO();
        dto.setId(ingredient.getId());
        dto.setRecipeId(ingredient.getRecipe().getId());
        dto.setName(ingredient.getName());
        dto.setAmount(ingredient.getAmount());
        return dto;
    }

    public  RecipeIngredient convertToEntity( RecipeIngredientDTO dto) {
         RecipeIngredient ingredient = new  RecipeIngredient();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        return ingredient;
    }

}