package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPostDto {
    private Long id;
    private String title;
    private int pricePerUser;
    private int type;
    private List<PostIngredientDto> ingredients;
    private List<PostImageDto> images;

    public static MyPostDto from(Post post, List<PostIngredientDto> ingredients, List<PostImageDto> images) {
        return MyPostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .pricePerUser(post.getPricePerUser())
                .type(post.getType())
                .ingredients(ingredients)
                .images(images)
                .build();
    }
}
