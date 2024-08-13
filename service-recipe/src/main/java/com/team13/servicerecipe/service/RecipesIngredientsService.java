package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipesIngredientsDTO;
import com.team13.servicerecipe.entity.RecipesIngredients;
import com.team13.servicerecipe.repository.RecipesIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipesIngredientsService {
    @Autowired
    private RecipesIngredientsRepository recipesIngredientsRepository;

    public RecipesIngredients saveIngredients(RecipesIngredients ingredient) {
        return recipesIngredientsRepository.save(ingredient);
    }

    public List<RecipesIngredients> getIngredientsByRecipeId(Long recipeId) {
        return recipesIngredientsRepository.findByRecipeId(recipeId);
    }
    public RecipesIngredientsDTO convertToDto(RecipesIngredients ingredient) {
        RecipesIngredientsDTO dto = new RecipesIngredientsDTO();
        dto.setId(ingredient.getId());
        dto.setRecipeId(ingredient.getRecipe().getId());
        dto.setName(ingredient.getName());
        dto.setAmount(ingredient.getAmount());
        return dto;
    }

    public RecipesIngredients convertToEntity(RecipesIngredientsDTO dto) {
        RecipesIngredients ingredient = new RecipesIngredients();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setAmount(dto.getAmount());
        return ingredient;
    }

}
