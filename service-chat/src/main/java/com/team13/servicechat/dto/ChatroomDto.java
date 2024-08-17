package com.team13.servicechat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatroomDto {
    private String id;
    private long postId;
    private List<Long> userIds;
    private List<ChatMessageDto> messages;
    private String lastMessage;
}
