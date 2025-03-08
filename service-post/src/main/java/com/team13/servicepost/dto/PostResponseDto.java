package com.team13.servicepost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseDto {
    private Long id;
    private Long userId; // 나중에 삭제 확인용
    private int status;
    private int groupSize;
    private int curGroupSize;
    private String chatId;
    private Date createdAt;
    private Date closedAt;
    private int locationBcode;
    private String locationAddress;
    private String locationLongitude;
    private String locationLatitude;
    private String title;
    private int pricePerUser;
    private int type;
    private String contents;
    private List<PostImageDto> images;
    private List<PostIngredientDto> ingredients;
    private String userNickname;
    private Long likesCount;
    private String userProfileUrl;
}
