package com.team13.n1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.team13.n1.wspacket.MessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    private String nickname;                                     // 유저 ID
    @JsonProperty("profile_image") private String profileImage;  // 프로필 이미지 경로
    private MessagePacket.MessageType type;                      // 메시지 타입
    private String message;                                      // 해당 유저가 전송한 메시지
}
