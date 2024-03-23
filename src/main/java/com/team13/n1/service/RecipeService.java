package com.team13.n1.service;

import com.team13.n1.entity.Recipe;
import com.team13.n1.entity.RecipeIngredient;
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

    private final UserService userService;
    private final PostService postService;

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

    public Map<String, Object> getRecipeById(int recipeId, String bCode) {
        Optional<Recipe> recipe = repository.findById(recipeId);
        return recipe.map(value -> recipeToHashMap(value, bCode))
                .orElseGet(HashMap::new);
    }


    // Recipe -> Hashmap 변환 (특정 정보만 저장)
    private Map<String, Object> recipeToSimpleHashMap(Recipe recipe) {
        Map<String, Object> hashmap = new HashMap<>();
        hashmap.put("id", recipe.getId());
        hashmap.put("title", recipe.getTitle());
        hashmap.put("thumbnail_image", recipe.getThumbnailImage());
        return hashmap;
    }

    // Recipe -> Hashmap 변환 (전체 정보 저장)
    private Map<String, Object> recipeToHashMap(Recipe recipe, String bCode) {
        Map<String, Object> hashmap = recipeToSimpleHashMap(recipe);

        Map<String, String> user = new HashMap<>();
        user.put("nickname", userService.getNicknameById(recipe.getUserId()));
        user.put("profile_image", userService.getProfileImageById(recipe.getUserId()));
        hashmap.put("user", user);

        hashmap.put("created_at", recipe.getCreatedAt());
        hashmap.put("likes_count", recipe.getLikesCount());

        hashmap.put("ingredients", recipe.getIngredients());
//        hashmap.put("processes", recipe.getProcesses());

        // linked_posts (주변 공동구매 게시글)
        List<Map<String, Object>> linkedPosts = new ArrayList<>();
        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            List<Map<String, Object>> foundPosts = postService.getList(bCode, "ingd", ingredient.getName(), "1");

            if (!foundPosts.isEmpty()) {
                Map<String, Object> foundPost = foundPosts.get(0);

                Map<String, Object> result = new HashMap<>();
                result.put("id", foundPost.get("id"));
                result.put("image", foundPost.get("image"));
                result.put("title", foundPost.get("title"));

                linkedPosts.add(result);
            }
        }
        hashmap.put("linked_posts", linkedPosts);

        return hashmap;
    }
}
