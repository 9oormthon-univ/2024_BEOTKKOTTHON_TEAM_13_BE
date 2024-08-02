package com.team13.servicechat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String chatroomsId;
    private long senderUsersId;
    private int type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private Date createdAt;
}
