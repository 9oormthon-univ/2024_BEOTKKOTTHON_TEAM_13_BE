package com.team13.n1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatBriefDto {
    @JsonProperty("chat_id") private String chatId;       // 채팅방 ID
    @JsonProperty("post_title") private String postTitle;       // 게시글 제목
    @JsonProperty("last_message") private String lastMessage;  // 채팅방 마지막 메시지
}
