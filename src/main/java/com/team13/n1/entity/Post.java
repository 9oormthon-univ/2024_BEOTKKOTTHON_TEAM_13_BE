package com.team13.n1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userId;
    private String title;
    private String price;
    private int type;
    private int status;
    private String images;
    private String url;
    private String contents;
    private String locationBcode;
    private String locationAddress;
    private String locationLongitude;
    private String locationLatitude;
    private int groupSize;
    private int curGroupSize;
    private String chatId;
    @CreationTimestamp private Date createdAt;
    private Date closedAt;

    @OneToMany
    private List<PostIngredient> ingredients;
}
