package com.team13.n1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @JoinColumn(name = "user_id")
    private String id;

    private String nickname;
    private int rating;
    private String profile;
}