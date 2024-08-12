package com.team13.servicechat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    // 메시지 타입 설명
    // MESSAGE_TEXT  - 사용자 텍스트 메시지
    // MESSAGE_IMAGE - 사용자 이미지 메시지
    // NOTICE        - 채팅 공지 ('~가 참여하였습니다.', '~가 퇴장하였습니다.'와 같은 공지성 메시지)
    // EXIT_USER     - 유저 퇴장 (해당 채팅방에서 유저가 공동구매 참여를 포기한 경우)
    // COMPLETE      - 공동구매 완료
    // ERROR         - 에러 메시지

    public enum MessageType { MESSAGE_TEXT, MESSAGE_IMAGE, NOTICE, EXIT_USER, COMPLETE, ERROR } // 패킷 메시지 타입

    private MessageType type;       // 메시지 타입
    private String message;         // 메시지 내용
    private Long senderUserId;    // 메시지 발신자 유저 ID
    private String senderUserName;  // 메시지 발신자 유저 이름
}
