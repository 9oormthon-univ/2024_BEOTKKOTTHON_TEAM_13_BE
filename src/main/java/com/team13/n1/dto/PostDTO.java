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
    private String postImages;
    private Integer postStatus;
    private String price;
    private Integer userLike;
    private String userNickname;
    private float userScore;
    private String ingredientName;
    private String ingredientUrl;
    private String contents;
    private Integer groupSize;
    private Integer curGroupSize;
    private String createdAt;
    private String locationAdress;
}
