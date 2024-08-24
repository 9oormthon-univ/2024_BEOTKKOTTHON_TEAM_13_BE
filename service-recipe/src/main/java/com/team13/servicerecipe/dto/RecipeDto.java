package com.team13.servicerecipe.dto;

import com.team13.servicerecipe.entity.RecipeIngredient;
import com.team13.servicerecipe.entity.RecipeProcess;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecipeDto {
    private Long id;
    private String userProfileUrl;
    private String userNickname;
    private String title;
    private String contents;
    private int commentCount;
    private int likesCount;
    private String thumbnailImagePath;
    private List<RecipeIngredient> ingredients;
    private List<RecipeProcess> processes;
}
