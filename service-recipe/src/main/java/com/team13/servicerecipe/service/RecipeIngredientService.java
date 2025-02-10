package com.team13.servicerecipe.service;

import com.team13.servicerecipe.dto.RecipeIngredientDto;
import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.repository.RecipeIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientService {

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository ;

    public RecipeIngredient saveIngredient(RecipeIngredient ingredient) {
        return recipeIngredientRepository.save(ingredient);
    }

    public List<RecipeIngredient> getIngredientsByRecipeId(Long recipeId) {
        return recipeIngredientRepository.findByRecipeId(recipeId);
    }

    public List<RecipeIngredientDto> getIngredientDtosByRecipeId(Long recipeId) {
        return getIngredientsByRecipeId(recipeId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public RecipeIngredientDto convertToDto(RecipeIngredient ingredient) {
        return RecipeIngredientDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .amount(ingredient.getAmount())
                .build();
    }

    public  RecipeIngredient convertToEntity( RecipeIngredientDto dto) {
        return RecipeIngredient.builder()
                .id(dto.getId())
                .name(dto.getName())
                .amount(dto.getAmount())
                .build();
    }

}