package com.team13.servicepost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private int groupSize;
    private int curGroupSize;
    private String chatId;
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
}

