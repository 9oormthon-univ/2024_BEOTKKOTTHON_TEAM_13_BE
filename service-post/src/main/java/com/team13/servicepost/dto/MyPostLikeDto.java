package com.team13.servicepost.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPostLikeDto {
    private Long id;
    private String title;
    private int pricePerUser;
    private int type;
    private List<PostIngredientDto> ingredients;
    private List<PostImageDto> images;
    private String userNickname;
}
