package com.team13.n1.repository;

import com.team13.n1.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Recipe, Integer> {
}
