package com.team13.serviceuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false, length = 20)
    private String password;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "user_rating", nullable = false)
    private float userRating;

    @Column(name = "location_bcode", nullable = false)
    private int locationBcode;

    @Column(name = "location_address", nullable = false, length = 100)
    private String locationAddress;

    @Column(name = "profile_image_url", nullable = false, length = 200)
    private String profileImageUrl;
}
