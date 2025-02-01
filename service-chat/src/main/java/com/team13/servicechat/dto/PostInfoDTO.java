package com.team13.servicechat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostInfoDTO {
    private String title;
    private String imagePath;
}