package com.team13.servicechat.dto;

import com.team13.servicechat.entity.ChatMessage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatroomDto {
    private String id;
    private String chat_token;
    private Long postId;
    private List<Long> userIds;
    private List<ChatMessageDto> messages;
    private Integer unreadMsgsCounter;
    private ChatMessage lastMessage;
    private PostInfoDTO post;
}