package com.team13.servicepost.service;

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
}
