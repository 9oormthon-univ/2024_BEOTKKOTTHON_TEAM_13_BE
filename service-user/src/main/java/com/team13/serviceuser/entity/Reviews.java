package com.team13.serviceuser.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="recipes_comments")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "posts_id", nullable = false)
    private Long postsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_users_id", nullable = false)
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_users_id", nullable = false)
    private User fromUser;

    @Column(name = "text", nullable = false, length = 500)
    private String text;

    @Column(name = "score", nullable = false, length = 100)
    private String score;

}
