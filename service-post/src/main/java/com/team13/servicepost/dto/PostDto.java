package com.team13.servicepost.dto;

import com.team13.servicepost.entity.PostImage;
import com.team13.servicepost.entity.PostIngredient;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class PostDto {
    private Long id;
    private int status;  // 0 - 마감, 1 - 진행중
    private String userNickname;
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
    private List<PostIngredient> ingredients;
    private List<PostImage> images;
}
