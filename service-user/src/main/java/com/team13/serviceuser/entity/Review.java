package com.team13.serviceuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="reviews")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posts_id", nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_users_id", nullable = false)
    private User toUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_users_id", nullable = false)
    private User fromUserId;

    @Column(name = "text", nullable = false, length = 500)
    private String text;

    @Column(name = "score", nullable = false, length = 100)
    private String score;

}
