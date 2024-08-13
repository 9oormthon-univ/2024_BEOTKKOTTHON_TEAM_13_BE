package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostsIngredientsDTO;
import com.team13.servicepost.entity.PostsIngredients;
import com.team13.servicepost.repository.PostsIngredientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostsIngredientsService {

    @Autowired
    private PostsIngredientsRepository postsIngredientsRepository;

    public PostsIngredients saveIngredient(PostsIngredients ingredient) {
        return postsIngredientsRepository.save(ingredient);
    }

    public List<PostsIngredients> getIngredientsByPostId(Long postId) {
        return postsIngredientsRepository.findByPostId(postId);
    }

    public PostsIngredientsDTO convertToDto(PostsIngredients ingredient) {
        PostsIngredientsDTO dto = new PostsIngredientsDTO();
        dto.setId(ingredient.getId());
        dto.setPostId(ingredient.getPost().getId());  // Set the Post ID instead of the Post entity
        dto.setName(ingredient.getName());
        dto.setUrl(ingredient.getUrl());
        return dto;
    }

    public PostsIngredients convertToEntity(PostsIngredientsDTO dto) {
        PostsIngredients ingredient = new PostsIngredients();
        ingredient.setId(dto.getId());
        ingredient.setName(dto.getName());
        ingredient.setUrl(dto.getUrl());
        // The Post entity should be set separately, based on the Post ID
        return ingredient;
    }
}