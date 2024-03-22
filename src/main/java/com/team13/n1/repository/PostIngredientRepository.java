package com.team13.n1.repository;

import com.team13.n1.entity.PostIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIngredientRepository extends JpaRepository<PostIngredient, Long> {
    // 특별한 메소드가 필요한 경우 여기에 추가할 수 있습니다.
}