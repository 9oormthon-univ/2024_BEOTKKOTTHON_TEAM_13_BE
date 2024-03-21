package com.team13.n1.entity;
import com.team13.n1.repository.PostIngredientRepository;
import com.team13.n1.repository.PostRepository;
import com.team13.n1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostIngredientRepository postIngredientRepository;

    private int ingredientCounter = 1; // 재료 번호 카운터

    @Override
    public void run(String... args) throws Exception {
        // 추가 포스트와 유저 데이터 생성
        for (int i = 1; i <= 5; i++) {
            String userProfile = "https://cdn.pixabay.com/photo/2014/11/30/14/11/cat-551554_640.jpg";
            String userNickname = "수현" + i;
            String postTitle  = "example_PostTitle " + i;
            String postImages = "https://cdn.pixabay.com/photo/2021/01/01/15/31/sushi-balls-5878892_1280.jpg";
            String contents = "레시피 요리 공구" + i;
            createUserAndPost(userProfile, userNickname, postTitle ,postImages , contents);
        }
    }

    private void createUserAndPost(String userProfile, String userNickname, String postTitle, String postImages, String contents) {
        User user = new User();
        user.setRating(userProfile.length()); // 임의의 값 설정
        user.setProfile(userProfile);
        user.setNickname(userNickname);
        userRepository.save(user);

        Random random = new Random();
        Integer num = random.nextInt(3) + 1;

        Post post = new Post();
        post.setPrice( num * 1000); // 임의의 값 설정
        post.setCreatedAt(LocalDateTime.now()); // 현재 시간 설정
        post.setClosedAt(LocalDateTime.now());
        post.setTitle(postTitle);
        post.setContents(contents);
        post.setImages(postImages);
        post.setGroupSize(num);
        post.setCurGroupSize(num); //숫자 제약 조건 걸어야함
        post.setUser(user);
        postRepository.save(post);

        int numberOfIngredients = random.nextInt(3) + 1; // 1, 2, 3 중 랜덤으로 결정

        for (int i = 0; i < numberOfIngredients; i++) {
            String ingredientName = "재료" + ingredientCounter;
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
