package com.team13.n1.service;

import com.team13.n1.entity.Post;
import com.team13.n1.entity.PostIngredient;
import com.team13.n1.entity.Recipe;
import com.team13.n1.entity.RecipeIngredient;
import com.team13.n1.repository.PostRepository;
import com.team13.n1.repository.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostService {
    private final int RECIPE_PER_PAGE = 20;

    private final PostRepository repository;
    private final RecipeIngredientRepository recipeIngredientRepo;
    private final ChatService chatService;

    private final UserService userService;
    private final UserSessionService userSessService;

    @Value("${image.upload.dir}")
    private String imageUploadDir;

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

    // 게시글 불러오기 (PostController용)
    public ResponseEntity<Map<String, Object>> getPost(int postId) {
        Optional<Post> post = repository.findById(postId);
        return post.map(value -> ResponseEntity.ok(postToHashMap(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 게시글 불러오기 (UserService용)
    public Map<String, Object> getSimplePostById(int postId) {
        Optional<Post> post = repository.findById(postId);
        return post.map(this::postToSimpleHashMap)
                .orElseGet(HashMap::new);
    }

    // 게시글 작성자 유저 ID로 게시글들 불러오기
    public List<Map<String, Object>> getSimplePostsByUserId(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Post post : repository.findByUserId(userId)) {
            result.add(postToSimpleHashMap(post));
        }
        return result;
    }

    // 게시글 title 반환
    public String getTitleById(int postId) {
        Optional<Post> post = repository.findById(postId);
        if (post.isPresent()) {
            return post.get().getTitle();
        }
        return "";
    }

    // 게시글 저장
    public String save(Map<String, Object> request) throws ParseException {
        if (userSessService.existsById((String) request.get("session_id"))) {
            String userId = userSessService.getUserIdBySessionId((String) request.get("session_id"));

            Post post = new Post();
            post.setUserId(userId);
            post.setImages((String)request.get("image"));
            post.setType(request.get("type").equals("ingd") ? 0 : 1);
            post.setTitle((String)request.get("title"));
            post.setContents((String)request.get("contents"));

            if (post.getType() == 1) {
                List<PostIngredient> ingredients = new ArrayList<>();
                for (Map<String, String> ingredient : (List<Map<String, String>>) request.get("ingredients")) {
                    PostIngredient postIngredient = new PostIngredient();
                    postIngredient.setName(ingredient.get("name"));
                    postIngredient.setUrl(ingredient.get("url"));
                    ingredients.add(postIngredient);
                }
                post.setIngredients(ingredients);
            } else {
                post.setUrl((String)request.get("url"));
            }

            post.setPrice((String)request.get("price"));
            post.setGroupSize(Integer.parseInt((String)request.get("group_size")));
            post.setLocationAddress((String)request.get("location_address"));
            post.setLocationBcode((String)request.get("location_bcode"));
            post.setLocationLongitude((String)request.get("location_longitude"));
            post.setLocationLatitude((String)request.get("location_latitude"));
            post.setClosedAt(new SimpleDateFormat("yyyy-MM-dd").parse((String)request.get("closed_at")));

            // 채팅방 생성
            String chatId = chatService.save(userId, ((Long)repository.count()).intValue() + 1);
            post.setChatId(chatId);

            repository.save(post);

            return chatId;
        }
        return "";
    }

    // 이미지 저장
    public String saveImage(MultipartFile imageFile) {
        String fileext = imageFile.getOriginalFilename().split("\\.")[1];
        String path = imageUploadDir + "/" + UUID.randomUUID() + "." + fileext;
        File file = new File(path);
        try {
            imageFile.transferTo(file);
        } catch (IOException e) {
            log.error(e);
        }
        return path;
    }


    // 게시글 전체 리스트
    private List<Map<String, Object>> getAllList(String bCode, String page) {
        List<Map<String, Object>> result = new ArrayList<>();
        int n = (Integer.parseInt(page) - 1) * RECIPE_PER_PAGE;

        List<Post> posts = bCode.isBlank() ? repository.findWithLimit(n, RECIPE_PER_PAGE) :
                repository.findWithLimit(bCode, n, RECIPE_PER_PAGE);
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

        // 식재료 공동구매인 경우 해당 식재료로 요리할 수 있는 관련 레시피 추가
        List<Map<String, Object>> linkedRecipes = new ArrayList<>();
        if (post.getType() == 0) {
            // 재료 이름으로 레시피 검색
            List<RecipeIngredient> ingredients = recipeIngredientRepo.findRecipeIngredientByName(post.getTitle());

            if (!ingredients.isEmpty()) {
                Recipe recipe = ingredients.get(ingredients.size() - 1).getRecipe();

                Map<String, Object> result = new HashMap<>();
                result.put("id", recipe.getId());
                result.put("thumbnail_image", recipe.getThumbnailImage());
                result.put("title", recipe.getTitle());

                linkedRecipes.add(result);
            }
        }
        hashmap.put("linked_recipes", linkedRecipes);

        return hashmap;
    }
}
