package com.team13.n1.service;

import com.team13.n1.entity.User;
import com.team13.n1.entity.UserLikesPost;
import com.team13.n1.entity.UserLikesRecipe;
import com.team13.n1.repository.UserLikesPostRepository;
import com.team13.n1.repository.UserLikesRecipeRepository;
import com.team13.n1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserLikesPostRepository likesPostRepo;
    private final UserLikesRecipeRepository likesRecipeRepo;

    // 해당 유저 ID가 있는지 여부
    public boolean existsById(String userId) {
        return repository.existsById(userId);
    }

    // 새로운 유저 등록
    public void addUser(String userId, String nickname) {
        repository.save(new User(userId, nickname));
    }

    // 유저 ID를 통해 닉네임 불러오기
    public String getNicknameById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get().getNickname();
            }
        }
        return "";
    }

    // 유저 ID를 통해 만족도 불러오기
    public double getUserRatingById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get().getUserRating();
            }
        }
        return 0d;
    }

    // 유저 ID를 통해 프로필 이미지 불러오기
    public String getProfileImageById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get().getProfileImage();
            }
        }
        return "";
    }

    // 유저 정보(닉네임, 만족도)
    public Map<String, Object> getUserInfo(String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put("nickname", getNicknameById(userId));
        user.put("user_rating", getUserRatingById(userId));
        return user;
    }

    // 유저의 공동구매 좋아요 추가
    public void likesPost(int postId, String userId) {
        UserLikesPost userLikesPost = new UserLikesPost();
        userLikesPost.setUser(getUserById(userId));
        userLikesPost.setPostId(postId);
        likesPostRepo.save(userLikesPost);
    }

    // 유저의 레시피 좋아요 추가
    public void likesRecipe(int recipeId, String userId) {
        UserLikesRecipe userLikesRecipe = new UserLikesRecipe();
        userLikesRecipe.setUser(getUserById(userId));
        userLikesRecipe.setRecipeId(recipeId);
        likesRecipeRepo.save(userLikesRecipe);
    }

    // 유저 객체 가져오기
    private User getUserById(String userId) {
        if (existsById(userId)) {
            Optional<User> user = repository.findById(userId);
            if (user.isPresent()) {
                return user.get();
            }
        }
        return new User();
    }
}
