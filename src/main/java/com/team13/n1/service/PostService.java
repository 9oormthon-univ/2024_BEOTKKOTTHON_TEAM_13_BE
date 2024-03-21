package com.team13.n1.service;

import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PostService {
    private final int RECIPE_PER_PAGE = 20;

    private final PostRepository repository;

    private final UserService userService;

    public List<Map<String, Object>> getList(String type, String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (type.equals("all")) {
            result.addAll(getAllList(page));
        } else {
            result.addAll(getTypeList(type, keyword, page));
        }

        return result;
    }


    private List<Map<String, Object>> getAllList(String page) {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        for (Post post : repository.findWithLimit(n, RECIPE_PER_PAGE)) {
            Map<String, Object> hashmap = postToHashMap(post);
            result.add(hashmap);
        }

        return result;
    }

    private List<Map<String, Object>> getTypeList(String type, String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        int intType = type.equals("r_ingd") ? 1 : 0;

        for (Post post : repository.findByKeywordAndTypeWithLimit(intType, keyword, n, RECIPE_PER_PAGE)) {
            Map<String, Object> hashmap = postToHashMap(post);
            result.add(hashmap);
        }

        return result;
    }

    private Map<String, Object> postToHashMap(Post post) {
        Map<String, Object> hashmap = new HashMap<>();
        hashmap.put("image", post.getImages());
        hashmap.put("price", post.getPrice());

        List<Map<String, String>> ingredients = new ArrayList<>();
        for (PostIngredient postIngredient : post.getIngredients()) {
            Map<String, String> ingredient = new HashMap<>();
            ingredient.put("name", postIngredient.getName());
            ingredient.put("url", postIngredient.getUrl());
            ingredients.add(ingredient);
        }
        hashmap.put("ingredients", ingredients);

        Map<String, String> location = new HashMap<>();
        location.put("longitude", post.getLocationLongitude());
        location.put("latitude", post.getLocationLatitude());
        hashmap.put("location", location);

        hashmap.put("id", post.getId());
        hashmap.put("title", post.getTitle());
        hashmap.put("type", post.getType() == 0 ? "ingd" : "r_ingd");
        hashmap.put("nickname", userService.getNicknameById(post.getUserId()));

        return hashmap;
    }
}
