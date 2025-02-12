package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.LikeRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRecipeRepository extends JpaRepository<LikeRecipe, Long> {
    Long countByRecipeId(Long recipeId);
    Optional<LikeRecipe> findByRecipeIdAndUserId(Long recipeId, Long userId);
    List<LikeRecipe> findByUserId(Long userId);
}
