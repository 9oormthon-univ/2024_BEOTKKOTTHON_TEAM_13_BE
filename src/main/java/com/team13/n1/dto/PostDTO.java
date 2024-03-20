package com.team13.n1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Integer postId;
    private String userId;
    private String chatId;
    private String title;
    private Integer userLike;
    private String postImages;
    private Integer postStatus;
    private String price;
    private String createdAt;
    private String userNickname;
    private float userScore;
    private String ingredientUrl;
    private String contents;
    private String ingredientName;
    private String locationAdress;
    private Integer groupSize;
    private Integer curGroupSize;

    //지도 내용 추가
}
