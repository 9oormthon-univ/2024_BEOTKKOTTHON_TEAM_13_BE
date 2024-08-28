package com.team13.servicepost.repository;

import com.team13.servicepost.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    List<PostImage> findByPostId(Long postId);
}