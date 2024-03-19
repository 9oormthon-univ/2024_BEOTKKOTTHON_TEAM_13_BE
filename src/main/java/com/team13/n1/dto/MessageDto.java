package com.team13.n1.dto;

import com.team13.n1.wspacket.MessagePacket;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    private String userId, message;          // 유저 ID, 해당 유저가 전송한 메시지
    private MessagePacket.MessageType type;  // 메시지 타입
}
