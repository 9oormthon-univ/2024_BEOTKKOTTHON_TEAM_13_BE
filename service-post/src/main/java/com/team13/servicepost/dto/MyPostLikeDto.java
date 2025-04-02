package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
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

    public static MyPostLikeDto from(Post post, List<PostIngredientDto> ingredients, List<PostImageDto> images, String userNickname) {
        return MyPostLikeDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .pricePerUser(post.getPricePerUser())
                .type(post.getType())
                .ingredients(ingredients)
                .images(images)
                .userNickname(userNickname)
                .build();
    }
}
