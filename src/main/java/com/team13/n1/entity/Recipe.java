package com.team13.n1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String thumbnailImage;
    private String userId;
    private String title;
    private int likesCount;
    private int commentsCount;

    @OneToMany
    private List<RecipeIngredient> ingredients;

    @OneToMany
    private List<RecipeProcess> processes;

    @OneToMany
    private List<RecipeComment> comments;
}
