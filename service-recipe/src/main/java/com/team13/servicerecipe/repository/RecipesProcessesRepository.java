package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.RecipesProcesses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipesProcessesRepository extends JpaRepository<RecipesProcesses, Long> {
    List<RecipesProcesses> findByRecipeId(Long recipeId);
}
