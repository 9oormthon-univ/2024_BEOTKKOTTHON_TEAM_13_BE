package com.team13.servicechat.controller;

import com.team13.servicechat.dto.MessageDto;
import com.team13.servicechat.dto.RoomDataDto;
import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.feign.UserFeignClient;
import com.team13.servicechat.service.ChatroomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final UserFeignClient userFeignClient; // 서비스 간 통신 테스트용 Feign Client

    private final ChatroomService chatroomService;        // 채팅방 정보를 가져오기 위한 서비스

    @GetMapping
    public String index() {
        return "Index page of service-chat";
    }

    @GetMapping("/config")
    public String getConfig() { return configTestString; }

    @GetMapping("/service-connection-test")
    public String serviceConnectionTest() {
        return userFeignClient.serviceConnectionTest().data();
    }


    // 채팅방 정보(공동구매 데이터 및 메시지 리스트) 반환
    @GetMapping("/room-data")
    public RoomDataDto getRoomData(@RequestParam String id) {

        // 채팅방 ID가 존재하는 경우에 실행
        if (chatroomService.existsChatroomId(id)) {
            Chatroom chatroom = chatroomService.getChatroomById(id); // 채팅방 정보

            // TODO: 채팅방 내에 현재 유저가 포함되는지 확인 (해당 채팅방에 권한이 있는지 확인)

            // 채팅방 정보 내에 저장된 메시지 ID 리스트로 메시지 내용을 불러옴
            List<MessageDto> messages = new ArrayList<>();

            for (long messageId : chatroom.getMessageIds()) {
                Optional<ChatMessage> opMessage = chatroomService.getMessageById(messageId);

                opMessage.ifPresent(message -> messages.add(MessageDto.builder()
                                .type(MessageDto.MessageType.valueOf(message.getType()))
                                .message(message.getMessage())
                                .senderUserId(message.getSenderUserId())
                                .senderUserName(message.getSenderUserName())
                                .build()));

                log.info(opMessage);
            }

            return RoomDataDto.builder()
                    .postId(chatroom.getPostId())
                    .messages(messages)
                    .build();
        }

        return RoomDataDto.builder().build();
    }

}