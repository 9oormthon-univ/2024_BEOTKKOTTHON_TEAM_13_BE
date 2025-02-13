package com.team13.servicerecipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="recipes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "users_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name="contents", nullable = false, length = 500)
    private String contents;

    @Column(name="comment_count", nullable = false)
    private int commentCount;

    @Column(name="likes_count", nullable = false)
    private int likesCount;

    @Column(name="thumbnail_image_path", nullable = false, length = 200)
    private String thumbnailImagePath;

}
