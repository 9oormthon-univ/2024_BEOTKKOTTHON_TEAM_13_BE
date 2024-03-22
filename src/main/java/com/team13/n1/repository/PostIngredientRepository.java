package com.team13.n1.repository;

import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostIngredientRepository extends JpaRepository<PostIngredient, Integer> {
    List<PostIngredient> findByPost(Post post);
}
