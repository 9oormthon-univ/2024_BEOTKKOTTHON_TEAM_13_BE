package com.team13.n1.repository;

import com.team13.n1.entity.UserLikesPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserLikesPostRepository extends JpaRepository<UserLikesPost, Integer> {
    List<UserLikesPost> findByUserId(String userId);
}
