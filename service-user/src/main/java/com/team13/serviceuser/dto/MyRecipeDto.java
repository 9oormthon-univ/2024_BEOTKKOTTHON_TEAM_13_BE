package com.team13.serviceuser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyRecipeDto {
    private Long id;
    private String title;
    private int likesCount;
    private String thumbnailImagePath;
}
