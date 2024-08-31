package com.team13.serviceuser.service;

import com.team13.serviceuser.dto.PostResponseDto;
import com.team13.serviceuser.dto.RecipeResponseDto;
import com.team13.serviceuser.dto.UserDto;
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
    public List<PostResponseDto> getPostsByUserId(Long userId) {
        return postServiceClient.getPostsByUserId(userId);
    }
    public List<RecipeResponseDto> getRecipesByUserId(Long userId) {
        return recipeServiceClient.getRecipesByUserId(userId);
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
