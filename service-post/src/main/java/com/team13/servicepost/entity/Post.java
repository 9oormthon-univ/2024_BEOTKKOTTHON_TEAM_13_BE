package com.team13.servicepost.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@Table(name = "posts")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "users_id", nullable = false)
    private Long usersId;

    @Column(name = "status", nullable = false)
    private int status;

    @Column(name = "group_size", nullable = false)
    private int groupSize;

    @Column(name = "cur_group_size", nullable = false)
    private int curGroupSize;

    @Column(name = "chat_id", nullable = false, length = 100)
    private String chatId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "closed_at", nullable = false)
    private Date closedAt;

    @Column(name = "location_bcode", nullable = false)
    private int locationBcode;

    @Column(name = "location_address", nullable = false, length = 100)
    private String locationAddress;

    @Column(name = "location_longitude", nullable = false, length = 100)
    private String locationLongitude;

    @Column(name = "location_latitude", nullable = false, length = 100)
    private String locationLatitude;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "price_per_user", nullable = false)
    private int pricePerUser;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "contents", nullable = false, length = 500)
    private String contents;
}