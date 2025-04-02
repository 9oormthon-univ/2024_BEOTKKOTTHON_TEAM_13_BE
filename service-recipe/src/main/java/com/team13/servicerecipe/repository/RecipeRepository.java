package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long userId);
    Optional<Recipe> findById(Long recipeId);
}
