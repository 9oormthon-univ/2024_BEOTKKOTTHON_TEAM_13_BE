package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.RecipesIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipesIngredientsRepository extends JpaRepository<RecipesIngredients, Long> {
    List<RecipesIngredients> findByRecipeId(Long recipeId);
}
