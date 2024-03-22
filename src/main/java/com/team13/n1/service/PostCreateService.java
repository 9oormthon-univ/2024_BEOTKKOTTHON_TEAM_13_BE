package com.team13.n1.service;

import com.team13.n1.dto.PostCreateRequest;
import com.team13.n1.dto.PostIngredientDto;
import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.PostIngredientRepository;
import com.team13.n1.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
// PostCreateService.java
@Service
public class PostCreateService {
    private final PostRepository postRepository;
    private final PostIngredientRepository postIngredientRepository;

    @Autowired
    public PostCreateService(PostRepository postRepository, PostIngredientRepository postIngredientRepository) {
        this.postRepository = postRepository;
        this.postIngredientRepository = postIngredientRepository;
    }

    public void createPost(PostCreateRequest request) {
        Post post = new Post();
        post.setUserId(request.getUserId());
        post.setTitle(request.getTitle());
        post.setPrice(request.getPrice());
        post.setImages(request.getImages());
        post.setGroupSize(request.getGroupSize());
        post.setContents(request.getContents());
        post.setClosedAt(new Date());
        // Set other fields similarly

        // Post를 저장
        Post savedPost = postRepository.save(post);

        // PostIngredient를 저장하기 전에 Post와 연관시켜야 함
        if (request.getIngredients() != null) {
            for (PostIngredientDto ingredientDto : request.getIngredients()) {
                PostIngredient ingredient = convertToEntity(ingredientDto);
                // Post와 연관시킴
                ingredient.setPost(savedPost);
                postIngredientRepository.save(ingredient);
            }
        }
    }

    private PostIngredient convertToEntity(PostIngredientDto ingredientDto) {
        PostIngredient ingredient = new PostIngredient();
        ingredient.setName(ingredientDto.getName());
        ingredient.setUrl(ingredientDto.getUrl());
        return ingredient;
    }
}
