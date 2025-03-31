package com.team13.serviceuser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyPostLikeDto {
    private Long id;
    private String title;
    private int pricePerUser;
    private int type;
    private List<PostIngredientDto> ingredients;
    private List<PostImageDto> images;
    private String userNickname;
}
