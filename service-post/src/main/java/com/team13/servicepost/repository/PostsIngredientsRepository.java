package com.team13.servicepost.repository;

import com.team13.servicepost.entity.PostsIngredients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsIngredientsRepository extends JpaRepository<PostsIngredients, Long> {
    List<PostsIngredients> findByPostId(Long postId);
}
