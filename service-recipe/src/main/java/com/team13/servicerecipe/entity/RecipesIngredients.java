package com.team13.servicerecipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="recipes_ingredients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipesIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipes_id", nullable = false)
    private Recipe recipe;

    @Column(name="name", nullable = false, length = 50)
    private String name;

    @Column(name="amount",nullable = false, length = 20)
    private String amount;


}
