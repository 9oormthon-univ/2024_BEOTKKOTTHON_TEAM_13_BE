package com.team13.n1.service;


import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.PostIngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PostIngredientServiceImpl implements PostIngredientService {

    @Autowired
    private PostIngredientRepository postIngredientRepository;

    @Override
    public List<PostIngredient> getIngredientsByPost(Post post) {
        return postIngredientRepository.findByPost(post);
    }
}
