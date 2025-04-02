package com.team13.servicerecipe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="recipes_ingredients")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "amount", nullable = false)
    private String amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipes_id", nullable = false)
    @JsonIgnore
    private Recipe recipe;
}
