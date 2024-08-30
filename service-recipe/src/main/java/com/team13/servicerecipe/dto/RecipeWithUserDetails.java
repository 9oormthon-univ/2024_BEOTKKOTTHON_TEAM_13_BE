package com.team13.servicerecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RecipeWithUserDetails {
    private Long id;
    private Long userId;
    private String title;
    private String contents;
    private int commentCount;
    private int likesCount;
    private String thumbnailImagePath;
    private Date createdAt;
    private int type;
    private String userNickname;
}