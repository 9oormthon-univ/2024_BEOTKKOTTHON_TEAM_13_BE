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
import java.util.List;

@Service
public class MyPageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostServiceClient postServiceClient;
    @Autowired
    private RecipeServiceClient recipeServiceClient;

    public MypageRecipeListResponseDto getRecipesByUserId(Long userId) {
        try {
            ApiResponse<MypageRecipeListResponseDto> response = recipeServiceClient.getRecipesByUserId(userId);

            if (response == null || !response.getIsSuccess() || response.getResult() == null) {
                return MypageRecipeListResponseDto.builder()
                        .userId(userId)
                        .userNickname(null)
                        .userProfileUrl(null)
                        .userRating(0.0f)
                        .recipes(Collections.emptyList())
                        .build();
            }

            return response.getResult();
        } catch (Exception e) {
            // 예외 발생 시 빈 리스트 반환
            return MypageRecipeListResponseDto.builder()
                    .userId(userId)
                    .userNickname(null)
                    .userProfileUrl(null)
                    .userRating(0.0f)
                    .recipes(Collections.emptyList())
                    .build();
        }
    }


    public MypageRecipeListResponseDto getLikeRecipesByUserId(Long userId) {
        ApiResponse<MypageRecipeListResponseDto> response = recipeServiceClient.getLikeRecipesByUserId(userId);

        if (response == null || !response.getIsSuccess() || response.getResult() == null) {
            // 빈 값으로 기본 객체 리턴
            return MypageRecipeListResponseDto.builder()
                    .userId(userId)
                    .userNickname(null)
                    .userProfileUrl(null)
                    .userRating(0.0f)
                    .recipes(Collections.emptyList())
                    .build();
        }

        return response.getResult();
    }

    public MypagePostListResponseDto<MyPostResponseDto> getPostsByUserId(Long userId) {
        return postServiceClient.getPostsByUserId(userId).getResult();
    }

    public MypagePostListResponseDto<MypagePostResponseDto> getLikePostsByUserId(Long userId) {
        return postServiceClient.getLikePostsByUserId(userId).getResult();
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
