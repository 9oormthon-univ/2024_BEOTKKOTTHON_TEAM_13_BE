package com.team13.serviceuser.service;

import com.team13.serviceuser.apiPyaload.ApiResponse;
import com.team13.serviceuser.dto.*;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.feign.PostServiceClient;
import com.team13.serviceuser.feign.RecipeServiceClient;
import com.team13.serviceuser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.function.Supplier;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostServiceClient postServiceClient;
    @Autowired
    private RecipeServiceClient recipeServiceClient;

    public MyRecipeListDto getRecipesByUserId(Long userId) {
        return fetchRecipeDataSafely(() -> recipeServiceClient.getRecipesByUserId(userId), userId);
    }

    public MyRecipeListDto getLikeRecipesByUserId(Long userId) {
        return fetchRecipeDataSafely(() -> recipeServiceClient.getLikeRecipesByUserId(userId), userId);
    }

    private MyRecipeListDto fetchRecipeDataSafely(
            Supplier<ApiResponse<MyRecipeListDto>> supplier,
            Long userId
    ) {
        try {
            ApiResponse<MyRecipeListDto> response = supplier.get();
            if (response == null || !response.getIsSuccess() || response.getResult() == null) {
                return emptyRecipeList(userId);
            }
            return response.getResult();
        } catch (Exception e) {
            return emptyRecipeList(userId);
        }
    }

    private MyRecipeListDto emptyRecipeList(Long userId) {
        return MyRecipeListDto.builder()
                .userId(userId)
                .userNickname(null)
                .userProfileUrl(null)
                .userRating(0.0f)
                .recipes(Collections.emptyList())
                .build();
    }

    public MyPostListDto<MyPostDto> getPostsByUserId(Long userId) {
        return fetchPostDataSafely(() -> postServiceClient.getPostsByUserId(userId));
    }

    public MyPostListDto<MyPostLikeDto> getLikePostsByUserId(Long userId) {
        return fetchPostDataSafely(() -> postServiceClient.getLikePostsByUserId(userId));
    }

    // 공통 처리 메서드 - Post 전용
    private <T> MyPostListDto<T> fetchPostDataSafely(Supplier<ApiResponse<MyPostListDto<T>>> supplier) {
        try {
            ApiResponse<MyPostListDto<T>> response = supplier.get();
            if (response == null || !response.getIsSuccess() || response.getResult() == null) {
                return new MyPostListDto<>(Collections.emptyList());
            }
            return response.getResult();
        } catch (Exception e) {
            return new MyPostListDto<>(Collections.emptyList());
        }
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
