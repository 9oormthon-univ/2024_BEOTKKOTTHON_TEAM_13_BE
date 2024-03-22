package com.team13.n1.service;



import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;

import java.util.List;

public interface PostIngredientService {
    List<PostIngredient> getIngredientsByPost(Post post);
}
