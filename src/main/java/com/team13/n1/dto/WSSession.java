package com.team13.n1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
public class WSSession {
    private String userId;               // 유저 ID
    private WebSocketSession wsSession;  // 해당 유저의 웹소켓 세션
}
