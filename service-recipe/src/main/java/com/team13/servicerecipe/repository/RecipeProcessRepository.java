package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.RecipeProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeProcessRepository extends JpaRepository<RecipeProcess, Long> {
    List<RecipeProcess> findByRecipeId(Long recipeId);
}
