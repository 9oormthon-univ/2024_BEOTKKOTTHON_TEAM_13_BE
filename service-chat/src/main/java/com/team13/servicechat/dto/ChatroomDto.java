package com.team13.servicechat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatroomDto {
    private long postId;
    private List<Long> userIds;
    private List<Long> messageIds;
}
