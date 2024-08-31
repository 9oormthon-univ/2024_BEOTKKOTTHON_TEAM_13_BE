package com.team13.serviceuser.service;

import com.team13.serviceuser.dto.*;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.feign.PostServiceClient;
import com.team13.serviceuser.feign.RecipeServiceClient;
import com.team13.serviceuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostServiceClient postServiceClient;
    @Autowired
    private RecipeServiceClient recipeServiceClient;
    public List<MypagePostResponseDto> getPostsByUserId(Long userId) {
        return postServiceClient.getPostsByUserId(userId);
    }
    public List<MypageRecipeResponseDto> getRecipesByUserId(Long userId) {
        return recipeServiceClient.getRecipesByUserId(userId);
    }
    public List<MypagePostResponseDto> getLikePostsByUserId(Long userId) {
        return postServiceClient.getLikePostsByUserId(userId);
    }

    public List<MypageRecipeResponseDto> getLikeRecipesByUserId(Long userId) {
        return recipeServiceClient.getLikeRecipesByUserId(userId);
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return convertToDto(user);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .userRating(user.getUserRating())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }


}
