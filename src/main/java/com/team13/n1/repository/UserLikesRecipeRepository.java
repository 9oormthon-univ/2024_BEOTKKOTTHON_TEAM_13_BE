package com.team13.n1.repository;

import com.team13.n1.entity.UserLikesRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLikesRecipeRepository extends JpaRepository<UserLikesRecipe, Integer> {
    List<UserLikesRecipe> findByUserId(String userId);
}
