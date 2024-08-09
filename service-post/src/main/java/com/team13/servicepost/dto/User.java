package com.team13.servicepost.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor

public class User {
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private float userRating;
    private int locationBcode;
    private String locationAddress;
    private String profileImageUrl;
}
