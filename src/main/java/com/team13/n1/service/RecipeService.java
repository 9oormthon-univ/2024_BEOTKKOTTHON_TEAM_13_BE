package com.team13.n1.service;

import com.team13.n1.entity.Recipe;
import com.team13.n1.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

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
            result.add(recipeToSimpleHashMap(recipe));
        }

        return result;
    }

    // 레시피 리스트
    public List<Map<String, Object>> getList(String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();

        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        for (Recipe recipe : repository.findByKeywordWithLimit(keyword, n, RECIPE_PER_PAGE)) {
            result.add(recipeToSimpleHashMap(recipe));
        }

        return result;
    }

    // 간단 레시피 불러오기
    public Map<String, Object> getSimpleRecipeById(int recipeId) {
        Optional<Recipe> recipe = repository.findById(recipeId);
        return recipe.map(this::recipeToSimpleHashMap)
                .orElseGet(HashMap::new);
    }

    // 게시글 작성자 유저 ID로 게시글들 불러오기
    public List<Map<String, Object>> getSimpleRecipesByUserId(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Recipe recipe : repository.findByUserId(userId)) {
            result.add(recipeToSimpleHashMap(recipe));
        }
        return result;
    }

    // Recipe -> Hashmap 변환 (특정 정보만 저장)
    private Map<String, Object> recipeToSimpleHashMap(Recipe recipe) {
        Map<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", recipe.getId());
        hashmap.put("title", recipe.getTitle());
        hashmap.put("thumbnail_image", recipe.getThumbnailImage());
        return hashmap;
    }
}
