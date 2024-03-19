package com.team13.n1.handler;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.team13.n1.service.ChatWSService;
import com.team13.n1.wspacket.MessagePacket;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


// 웹소켓 패킷은 핸들러를 통해 수신됨
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper = JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS) // ignore escape character
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES) // allow undeclared properties
            .build();
    private final ChatWSService service;

    // 메시지 수신
    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        MessagePacket packet = mapper.readValue(message.getPayload(), MessagePacket.class);

        System.out.println(session.getId() + " -> " + packet);

        if (packet.getType() == MessagePacket.MessageType.ENTER) {                   // 사용자 입장
            service.enter(session, packet);
        } else if (packet.getType() == MessagePacket.MessageType.MESSAGE_TEXT ||     // 사용자 메시지 전송
                packet.getType() == MessagePacket.MessageType.MESSAGE_IMAGE) {
            service.message(session, packet);
        } else if (packet.getType() == MessagePacket.MessageType.EXIT_SESSION) {     // 사용자 세션이 채팅방을 나간 경우
            service.sessionExit(packet);
        } else if (packet.getType() == MessagePacket.MessageType.COMPLETE) {         // 공동구매 완료 메시지
            service.complete(session, packet);
        }
    }

    // 웹소켓 세션 종료 후 실행됨
    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}
