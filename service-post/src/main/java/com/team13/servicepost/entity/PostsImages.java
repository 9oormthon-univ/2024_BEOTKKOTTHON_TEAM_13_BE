package com.team13.servicepost.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts_images")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostsImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posts_id", nullable = false)
    private Post post;

    @Column(name = "image_path", nullable = false, length = 200)
    private String imagePath;
}
