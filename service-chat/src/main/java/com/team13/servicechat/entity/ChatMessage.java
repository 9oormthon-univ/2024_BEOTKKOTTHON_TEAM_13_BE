package com.team13.servicechat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity(name = "chat_messages")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "chatrooms_id")
    private String chatroomId;

    @Column(name = "sender_users_id")
    private long senderUserId;
    private String type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    private Date createdAt;
}
