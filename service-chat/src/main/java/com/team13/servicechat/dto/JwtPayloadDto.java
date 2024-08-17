package com.team13.servicechat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtPayloadDto {
    private String userId;
    private String userNickname;
}
