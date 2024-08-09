package com.team13.servicepost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostWithUserDetails {
    private Long id;
    private Long usersId;
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
    private int price;
    private int pricePerUser;
    private int type;
    private String contents;
    private String userNickname;
}
