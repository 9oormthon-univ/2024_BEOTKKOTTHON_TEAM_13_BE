package com.team13.servicepost.service;

import com.team13.servicepost.dto.PostImageDto;
import com.team13.servicepost.dto.PostIngredientDto;
import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;
import com.team13.servicepost.repository.PostIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostIngredientService {

    @Autowired
    private PostIngredientRepository postIngredientRepository;

    public PostIngredient saveIngredient(PostIngredient ingredient) {
        return  postIngredientRepository.save(ingredient);
    }

    public List<PostIngredient> getIngredientsByPostId(Long postId) {
        return postIngredientRepository.findByPostId(postId);
    }

    public PostIngredientDto convertToDto(PostIngredient ingredient) {
        PostIngredientDto dto = new PostIngredientDto();
        dto.setId(ingredient.getId());
        dto.setPostId(ingredient.getPost().getId());
        dto.setName(ingredient.getName());
        dto.setUrl(ingredient.getUrl());
        return dto;
    }

}
