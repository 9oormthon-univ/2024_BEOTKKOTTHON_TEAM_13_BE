package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {
    private int groupSize;
    private int curGroupSize;
    private String chatId;
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

    public Post toEntity() {
        Post post = new Post();
        post.setGroupSize(this.groupSize);
        post.setCurGroupSize(this.curGroupSize);
        post.setChatId(this.chatId);
        post.setClosedAt(this.closedAt);
        post.setLocationBcode(this.locationBcode);
        post.setLocationAddress(this.locationAddress);
        post.setLocationLongitude(this.locationLongitude);
        post.setLocationLatitude(this.locationLatitude);
        post.setTitle(this.title);
        post.setPricePerUser(this.pricePerUser);
        post.setType(this.type);
        post.setContents(this.contents);
        post.setCreatedAt(new Date());
        return post;
    }
}
