package com.team13.servicechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomMessage {
    public enum MessageType { ENTER, MESSAGE_TEXT, MESSAGE_IMAGE, NOTICE, EXIT_USER, EXIT_SESSION, COMPLETE, ERROR } // 패킷 메시지 타입
    // ENTER - 채팅방 입장 (사전에 채팅방에 등록된 유저의 세션이 채팅방에 입장하는 경우)
    // MESSAGE_TEXT - 사용자 텍스트 메시지
    // MESSAGE_IMAGE - 사용자 이미지 메시지
    // NOTICE - 채팅 공지 ('~가 참여하였습니다.', '~가 퇴장하였습니다.'와 같은 공지성 메시지)
    // EXIT_USER - 유저 퇴장 (해당 채팅방에서 유저가 공동구매 참여를 포기한 경우)
    // EXIT_SESSION - 유저 세션 퇴장 (단순히 사용자가 채팅방 화면 밖으로 나간 경우)
    // COMPLETE - 공동구매 완료
    // ERROR - 에러 메시지

    private MessageType type;     // 메시지 타입
    private String message;       // 메시지 내용
    private String senderUserId;  // 메시지 발신자 유저 ID
}
