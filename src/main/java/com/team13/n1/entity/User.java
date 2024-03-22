package com.team13.n1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String nickname;
    private double userRating;
    private String profileImage;

    public User(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        userRating = 0d;
        profileImage = "";
    }
}