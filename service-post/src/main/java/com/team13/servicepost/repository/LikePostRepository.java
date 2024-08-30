package com.team13.servicepost.repository;

import com.team13.servicepost.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Long countByPostId(Long postId);
    Optional<LikePost> findByPostIdAndUserId(Long postId, Long userId);

}
