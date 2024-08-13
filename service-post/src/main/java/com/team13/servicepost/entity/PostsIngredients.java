package com.team13.servicepost.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts_ingredients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    @JsonIgnore
    private Post post;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

    @Column(name = "url", nullable = false, length = 300)
    private String url;
}