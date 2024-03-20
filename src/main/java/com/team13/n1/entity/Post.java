package com.team13.n1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;

@Entity

@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer post_id; //(PK)
    private String user_Id; //(FK)
    private String chat_id; //(FK)
    private String title;
    private String post_images;
    private Integer post_status;
    private String price;
    private Integer user_like;
    private String user_nickname;
    private float user_score;
    private String ingredient_name;
    private String ingredient_url;
    private String contents;
    private Integer group_size;
    private Integer cur_group_size;
    private String created_at; //나중에 Date로 변경
}

