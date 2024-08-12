package com.team13.servicechat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RoomDataDto {
    // TODO: 공동구매 게시글 관련 데이터
    private long postId;

    // 채팅방 메시지들
    private List<MessageDto> messages;
}
