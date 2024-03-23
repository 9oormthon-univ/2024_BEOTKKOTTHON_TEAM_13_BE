package com.team13.n1.repository;

import com.team13.n1.entity.Recipe;
import com.team13.n1.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findRecipeIngredientByName(String name);
    List<RecipeIngredient> findByRecipe(Recipe recipe);
}
