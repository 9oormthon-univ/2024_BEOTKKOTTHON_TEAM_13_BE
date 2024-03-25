package com.team13.n1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
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
    @CreationTimestamp private Date createdAt;

    @OneToMany
    @JoinColumn(name="recipe_id")
    private List<RecipeIngredient> ingredients;

    @OneToMany
    @JoinColumn(name="recipe_id")
    private List<RecipeProcess> processes;

    @OneToMany
    @JoinColumn(name="recipe_id")
    private List<RecipeComment> comments;
}
