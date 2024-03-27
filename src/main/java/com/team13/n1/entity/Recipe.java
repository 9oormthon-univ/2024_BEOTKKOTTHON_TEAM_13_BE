package com.team13.n1.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
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


    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeIngredient> ingredients = new ArrayList<>(); // 리스트를 초기화합니다.
    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this); // 양방향 관계 설정
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeProcess> processes = new ArrayList<>();
    public void addProcess(RecipeProcess process) {
        this.processes.add(process);
        process.setRecipe(this);
    }

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<RecipeComment> comments = new ArrayList<>();

    public void addComment(RecipeComment comment) {
        this.comments.add(comment);
        comment.setRecipe(this);
    }

}
