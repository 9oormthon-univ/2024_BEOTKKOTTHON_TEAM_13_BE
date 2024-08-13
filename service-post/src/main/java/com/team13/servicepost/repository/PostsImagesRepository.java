package com.team13.servicepost.repository;

import com.team13.servicepost.entity.PostsImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsImagesRepository extends JpaRepository<PostsImages, Long> {
    List<PostsImages> findByPostId(Long postId);
}