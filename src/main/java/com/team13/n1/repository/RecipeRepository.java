package com.team13.n1.repository;

import com.team13.n1.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

    // 랜덤 레시피 n개 선택
    @Query(value="select * from recipe order by RAND() limit :n", nativeQuery = true)
    List<Recipe> findRandomRecipes(@Param("n") int n);

    // 레시피 탐색
    @Query(value="select * from recipe where title like %:keyword% limit :n, :m", nativeQuery = true)
    List<Recipe> findByKeywordWithLimit(@Param("keyword") String keyword, @Param("n") int n, @Param("m") int m);
}