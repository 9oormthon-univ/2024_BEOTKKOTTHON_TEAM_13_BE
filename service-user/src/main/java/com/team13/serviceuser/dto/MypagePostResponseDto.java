package com.team13.serviceuser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MypagePostResponseDto {
    private Long id;
    private Long userId;
    private int status;
    private int groupSize;
    private int curGroupSize;
    private Date createdAt;
    private Date closedAt;
    private String title;
    private int pricePerUser;
    private int type;
    private String contents;
    private String userNickname;
    private List<PostIngredientDto> ingredients;
    private Long likesCount;
}