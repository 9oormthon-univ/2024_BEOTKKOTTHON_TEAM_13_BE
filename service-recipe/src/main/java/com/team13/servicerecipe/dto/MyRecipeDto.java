package com.team13.servicerecipe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyRecipeDto {
    private Long id;
    private String thumbnailImagePath;
}
