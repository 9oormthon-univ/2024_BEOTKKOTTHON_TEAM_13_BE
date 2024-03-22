package com.team13.n1.service;

import com.team13.n1.entity.UserLikesPost;
import com.team13.n1.entity.UserLikesRecipe;
import com.team13.n1.repository.UserLikesPostRepository;
import com.team13.n1.repository.UserLikesRecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserLikesService {
    private final PostService postService;
    private final RecipeService recipeService;
    private final UserLikesPostRepository likesPostRepo;
    private final UserLikesRecipeRepository likesRecipeRepo;

    // 유저가 좋아요 한 공동구매 게시글 불러오기
    public List<Map<String, Object>> getUserLikesPosts(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserLikesPost likesPost : likesPostRepo.findByUserId(userId)) {
            result.add(postService.getSimplePostById(likesPost.getPostId()));
        }
        return result;
    }

    // 유저가 좋아요 한 레시피 게시글 불러오기
    public List<Map<String, Object>> getUserLikesRecipes(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserLikesRecipe likesPost : likesRecipeRepo.findByUserId(userId)) {
            result.add(recipeService.getSimpleRecipeById(likesPost.getRecipeId()));
        }
        return result;
    }
}
