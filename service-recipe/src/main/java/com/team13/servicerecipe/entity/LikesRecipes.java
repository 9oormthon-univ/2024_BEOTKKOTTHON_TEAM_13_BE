package com.team13.servicerecipe.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes_recipes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikesRecipes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipes_id", nullable = false)
    private Recipe recipe;

    @Column(name = "users_id", nullable = false)
    private Long usersId;
}
