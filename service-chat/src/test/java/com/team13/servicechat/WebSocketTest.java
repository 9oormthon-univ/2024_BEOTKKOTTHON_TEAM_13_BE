package com.team13.servicechat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=dev")
class WebSocketTest {
    private static WebSocketStompClient stompClient;
    private static StompSession stompSession;

    @BeforeAll
    public static void init(@Value("${server.port}") int port,
                            @Value("${app.test-token}") String testToken) throws Exception {
        // 웹소켓 클라이언트 초기화
        stompClient = new WebSocketStompClient(
                new SockJsClient(
                        List.of(new WebSocketTransport(
                                new StandardWebSocketClient()
                        ))
                )
        );

        // 메시지 컨버터 지정
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // 웹소켓 연결
        String url = String.format("ws://localhost:%d/ws", port); // WS URL

        // 웹소켓 핸드셰이크 헤더 지정
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();

        // 웹소켓 헤더 지정
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Authorization", testToken);

        // 웹소켓 핸들러
        StompSessionHandlerAdapter stompSessionHandlerAdapter = new StompSessionHandlerAdapter() {};

        // 웹소켓 연결
        stompSession = stompClient.connectAsync(url,
                webSocketHttpHeaders, stompHeaders, // 헤더 설정
                stompSessionHandlerAdapter).get();
    }

    @Test
    public void WebSocket_Connect() {

    }
}