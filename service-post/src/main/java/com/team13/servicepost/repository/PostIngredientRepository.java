package com.team13.servicepost.repository;

import com.team13.servicepost.entity.PostIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostIngredientRepository extends JpaRepository<PostIngredient, Long> {
    List<PostIngredient> findByPostId(Long postId);
}
