package com.team13.servicepost.dto;

import com.team13.servicepost.entity.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PostImageDTO {
    private Long id;
    private Long postId;
    private String imagePath;
}
