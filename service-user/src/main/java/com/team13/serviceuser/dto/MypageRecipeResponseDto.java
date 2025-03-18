package com.team13.serviceuser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MypageRecipeResponseDto {
    private Long id;
    private String thumbnailImagePath;
}
