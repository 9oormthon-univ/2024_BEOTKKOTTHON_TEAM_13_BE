package com.team13.n1.entity;


import com.team13.n1.repository.PostIngredientRepository;
import com.team13.n1.repository.PostRepository;
import com.team13.n1.repository.UserLikePostRepository;
import com.team13.n1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PlusDataLoader implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostIngredientRepository postIngredientRepository;

    @Autowired
    private UserLikePostRepository userLikePostRepository;

    private int ingredientCounter = 1; // 재료 번호 카운터

    @Override
    public void run(String... args) throws Exception {
        // 사용자 및 게시물 생성
        createUserAndPost("aaa", "수현", "게시물 제목 1", "게시물 이미지 1", "수현의 포스트 내용");
        createUserAndPost("aaa", "수현", "게시물 제목 2", "게시물 이미지 2", "수현의 포스트 내용");
        createUserAndPost("bbb", "준영", "게시물 제목 3", "게시물 이미지 3", "준영의 포스트 내용");
        createUserAndPost("bbb", "준영", "게시물 제목 4", "게시물 이미지 4", "준영의 포스트 내용");

        // 좋아요 설정
        setPostLikes("aaa", 3); // 수현이가 post id가 3인 게시물을 좋아함
        setPostLikes("aaa", 4); // 수현이가 post id가 4인 게시물을 좋아함
        setPostLikes("bbb", 1); // 준영이가 post id가 1인 게시물을 좋아함
        setPostLikes("bbb", 2); // 준영이가 post id가 2인 게시물을 좋아함
    }

    private void createUserAndPost(String userId, String userNickname, String postTitle, String postImages, String contents) {
        User user = new User();
        user.setId(userId);
        user.setRating(userNickname.length()); // 임의의 값 설정
        user.setProfile("프로필");
        user.setNickname(userNickname);
        userRepository.save(user);

        Post post = new Post();
        post.setPrice(1000); // 임의의 값 설정
        post.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        post.setClosedAt(LocalDateTime.now());
        post.setTitle(postTitle);
        post.setImages(postImages);
        post.setGroupSize(5);
        post.setCurGroupSize(2);
        post.setUser(user);
        post.setContents(contents); // 게시물 내용 입력
        postRepository.save(post);

        // 랜덤하게 재료 추가
        addRandomIngredients(post);
    }

    private void setPostLikes(String userId, int postId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            UserLikePost userLikePost = new UserLikePost();
            userLikePost.setUser(user);
            userLikePost.setPost(postId); // postId 설정
            userLikePostRepository.save(userLikePost);
        }
    }

    private void addRandomIngredients(Post post) {
        Random random = new Random();
        int numberOfIngredients = random.nextInt(3) + 1; // 1에서 3개의 재료 랜덤 선택
        List<String> ingredients = new ArrayList<>();
        ingredients.add("감자");
        ingredients.add("사과");
        ingredients.add("당근");
        ingredients.add("포도");

        for (int i = 0; i < numberOfIngredients; i++) {
            String ingredientName = ingredients.get(random.nextInt(ingredients.size()));
            String ingredientUrl = "https://www.coupang.com/vp/products/6424880263?itemId=19262068639&vendorItemId=86377502239&sourceType=cmgoms&omsPageId=113873&omsPageUrl=113873&isAddedCart=";
            PostIngredient ingredient = new PostIngredient();
            ingredient.setName(ingredientName);
            ingredient.setUrl(ingredientUrl);
            ingredient.setPost(post);
            postIngredientRepository.save(ingredient);
            ingredientCounter++; // 재료 번호 카운터 증가
        }
    }
}
