package com.team13.servicerecipe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="recipes_processes")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipesProcesses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipes_id", nullable = false)
    private Recipe recipe;

    @Column(name="image_path", nullable = false, length = 200)
    private String imagePath;

    @Column(name="contents", nullable = false, length = 500)
    private String contents;
}
