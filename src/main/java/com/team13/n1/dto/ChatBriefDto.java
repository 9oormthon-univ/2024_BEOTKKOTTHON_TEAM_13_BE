package com.team13.n1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatBriefDto {
    private String chatId;       // 채팅방 ID
    private String postId;       // 게시글 ID
    private String lastMessage;  // 채팅방 마지막 메시지
}
