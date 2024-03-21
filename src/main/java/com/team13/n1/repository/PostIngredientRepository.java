package com.team13.n1.repository;

import com.team13.n1.entity.PostIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIngredientRepository extends JpaRepository<PostIngredient, Integer> {
}
