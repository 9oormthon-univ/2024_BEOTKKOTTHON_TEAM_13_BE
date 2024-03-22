package com.team13.n1.service;

import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {
    private final int RECIPE_PER_PAGE = 20;

    private final PostRepository repository;
    private final UserService userService;

    // 게시글 리스트
    public List<Map<String, Object>> getList(String bCode, String type, String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (type.equals("all")) {
            result.addAll(getAllList(bCode, page));
        } else {
            result.addAll(getTypeList(bCode, type, keyword, page));
        }

        return result;
    }

    // 게시글 불러오기
    public ResponseEntity<Map<String, Object>> getPost(int postId) {
        Optional<Post> post = repository.findById(postId);
        return post.map(value -> ResponseEntity.ok(postToHashMap(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    // 게시글 전체 리스트
    private List<Map<String, Object>> getAllList(String bCode, String page) {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        List<Post> posts = bCode.isBlank() ? repository.findWithLimit(n, RECIPE_PER_PAGE) :
                repository.findWithLimit(bCode, n, RECIPE_PER_PAGE);
        log.info(bCode);
        log.info(bCode.isBlank());
        for (Post post : posts) {
            Map<String, Object> hashmap = postToSimpleHashMap(post);
            result.add(hashmap);
        }

        return result;
    }

    // 게시글 특정 타입 리스트
    private List<Map<String, Object>> getTypeList(String bCode, String type, String keyword, String page) {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        int intType = type.equals("r_ingd") ? 1 : 0;

        List<Post> posts = bCode.isBlank() ?
                repository.findByKeywordAndTypeWithLimit(intType, keyword, n, RECIPE_PER_PAGE) :
                repository.findByKeywordAndTypeWithLimit(bCode, intType, keyword, n, RECIPE_PER_PAGE);
        for (Post post : posts) {
            Map<String, Object> hashmap = postToSimpleHashMap(post);
            result.add(hashmap);
        }

        return result;
    }

    // Post -> Hashmap 변환 (리스트에서 보여질 정보만 저장)
    private Map<String, Object> postToSimpleHashMap(Post post) {
        Map<String, Object> hashmap = new HashMap<>();
        hashmap.put("image", post.getImages());
        hashmap.put("price", post.getPrice());
        hashmap.put("group_size", post.getGroupSize());
        hashmap.put("cur_group_size", post.getCurGroupSize());
        hashmap.put("closed_at", post.getClosedAt());

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
        location.put("address", post.getLocationAddress());
        hashmap.put("location", location);

        hashmap.put("id", post.getId());
        hashmap.put("title", post.getTitle());
        hashmap.put("type", post.getType() == 0 ? "ingd" : "r_ingd");
        hashmap.put("nickname", userService.getNicknameById(post.getUserId()));

        return hashmap;
    }

    // Post -> Hashmap 변환 (전체 필드 변환)
    private Map<String, Object> postToHashMap(Post post) {
        Map<String, Object> hashmap = postToSimpleHashMap(post);

        hashmap.put("status", post.getStatus());
        hashmap.put("contents", post.getContents());
        hashmap.put("url", post.getUrl());
        hashmap.put("chat_id", post.getChatId());
        hashmap.put("created_at", post.getCreatedAt());

        return hashmap;
    }
}
