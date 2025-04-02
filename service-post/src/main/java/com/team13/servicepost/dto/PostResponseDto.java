package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
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
public class PostResponseDto {
    private Long id;
    private Long userId; // 나중에 삭제 확인용
    private int status;
    private int groupSize;
    private int curGroupSize;
    private String chatId;
    private Date createdAt;
    private Date closedAt;
    private int locationBcode;
    private String locationAddress;
    private String locationLongitude;
    private String locationLatitude;
    private String title;
    private int pricePerUser;
    private int type;
    private String contents;
    private List<PostImageDto> images;
    private List<PostIngredientDto> ingredients;
    private String userNickname;
    private Long likesCount;
    private String userProfileUrl;
    private float userRating;

    public static PostResponseDto from(Post post, UserDto userDto,
                                       List<PostIngredientDto> ingredients,
                                       List<PostImageDto> images,
                                       Long likesCount) {
        return PostResponseDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .status(post.getStatus())
                .groupSize(post.getGroupSize())
                .curGroupSize(post.getCurGroupSize())
                .chatId(post.getChatId())
                .createdAt(post.getCreatedAt())
                .closedAt(post.getClosedAt())
                .locationBcode(post.getLocationBcode())
                .locationAddress(post.getLocationAddress())
                .locationLongitude(post.getLocationLongitude())
                .locationLatitude(post.getLocationLatitude())
                .title(post.getTitle())
                .pricePerUser(post.getPricePerUser())
                .type(post.getType())
                .contents(post.getContents())
                .ingredients(ingredients)
                .images(images)
                .userNickname(userDto.getNickname())
                .userProfileUrl(userDto.getProfileImageUrl())
                .userRating(userDto.getUserRating())
                .likesCount(likesCount)
                .build();
    }
}
