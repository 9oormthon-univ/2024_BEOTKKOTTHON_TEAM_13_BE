package com.team13.servicepost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private Long id;
    private String nickname;
    private float userRating;
    private String profileImageUrl;
}
