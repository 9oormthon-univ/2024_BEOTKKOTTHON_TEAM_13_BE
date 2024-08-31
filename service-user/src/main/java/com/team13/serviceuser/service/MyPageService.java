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

    // 사용자 정보 업데이트 메서드
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        // 업데이트할 사용자 정보 설정
        user.setEmail(userDto.getEmail());
        user.setNickname(userDto.getNickname());
        user.setUserRating(userDto.getUserRating());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        // 비밀번호는 업데이트하는 로직에 따라 처리 필요 (예: 암호화된 상태로 저장)

        // 변경된 사용자 정보 저장
        userRepository.save(user);

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
