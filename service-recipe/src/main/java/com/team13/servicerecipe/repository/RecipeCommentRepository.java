package com.team13.servicerecipe.repository;

import com.team13.servicerecipe.entity.RecipeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeCommentRepository extends JpaRepository<RecipeComment, Long> {
    List<RecipeComment> findByRecipeId(Long recipeId);

    List<RecipeComment> findByParentCommentId(Long parentCommentId);

    List<RecipeComment> findByUserId(Long userId);


}