package com.team13.n1.service;

import com.team13.n1.entity.Recipe;
import com.team13.n1.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecipeService {
    private final int RECIPE_PER_PAGE = 20;

    private final RecipeRepository repository;

    // 10개의 랜덤 레시피 가져오기
    public List<Map<String, Object>> getBriefList() {
        List<Map<String, Object>> result = new ArrayList<>();

        for (Recipe recipe : repository.findRandomRecipes(10)) {
            Map<String, Object> resultRecipe = new HashMap<>();
            resultRecipe.put("id", recipe.getId());
            resultRecipe.put("title", recipe.getTitle());
            resultRecipe.put("thumbnail_image", recipe.getThumbnailImage());
            result.add(resultRecipe);
        }

        return result;
    }

    // 레시피 리스트
    public List<Map<String, Object>> getList(String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();

        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        for (Recipe recipe : repository.findByKeywordWithLimit(keyword, n, RECIPE_PER_PAGE)) {
            Map<String, Object> resultRecipe = new HashMap<>();
            resultRecipe.put("id", recipe.getId());
            resultRecipe.put("title", recipe.getTitle());
            resultRecipe.put("thumbnail_image", recipe.getThumbnailImage());
            result.add(resultRecipe);
        }

        return result;
    }
}
