package com.team13.servicerecipe.dto;

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
public class RecipeCommentDto {
    private Long id;
    private String comment;
    private Date createdAt;
    private Long userId;
    private Long recipeId;
    private Long parentCommentId;
    private String userNickname;
    private String profileImageUrl;
    private List<RecipeCommentDto> replies;

}

