package com.team13.n1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int recipeId;
    private String image;
    private String contents;
}
