package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long userId);
    Optional<Recipe> findById(Long recipeId);

    // NOTE: 랜덤 레시피 추출
    @Query(value = "SELECT * FROM recipes ORDER BY RAND() LIMIT :n", nativeQuery = true)
    List<Recipe> findRandom(@Param("n") int n);
}
