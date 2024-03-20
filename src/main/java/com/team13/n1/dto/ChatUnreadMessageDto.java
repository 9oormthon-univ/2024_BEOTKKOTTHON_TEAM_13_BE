package com.team13.n1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatUnreadMessageDto {
    @JsonProperty("chat_id") private String chatId;   // 채팅방 ID
    private String message;  // 채팅방 미확인 메시지
}
