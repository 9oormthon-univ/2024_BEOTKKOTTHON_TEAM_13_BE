package com.team13.servicechat.controller;

import com.team13.servicechat.dto.ChatroomDto;
import com.team13.servicechat.dto.JwtPayloadDto;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.feign.UserFeignClient;
import com.team13.servicechat.service.ChatroomService;
import com.team13.servicechat.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    @Value("${app.test-string}")
    private String configTestString;

    private final UserFeignClient userFeignClient;    // 서비스 간 통신 테스트용 Feign Client

    private final ChatroomService chatroomService;    // 채팅방 정보를 가져오기 위한 서비스
    private final JwtService jwtService;              // JWT 토큰 관리를 위한 서비스


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
    @GetMapping("/chatroom")
    public ResponseEntity<ChatroomDto> getChatroom(@CookieValue("LTK") String loginToken,
                                                   @RequestParam("id") String chatroomId) {

        JwtPayloadDto payload = jwtService.getPayloadFromToken(loginToken);

        // 채팅방 ID가 존재하고, 채팅방 ID에 해당 유저가 채팅방에 속하는 경우에만 결과 반환
        if (chatroomService.existsChatroomId(chatroomId) &&
            chatroomService.verifyUserInChatroom(payload.getUserId(), chatroomId)) {

            return ResponseEntity.ok(chatroomService.getChatroomDtoById(chatroomId,
                    Long.parseLong(payload.getUserId())));
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // 사용자가 속한 채팅방 리스트 반환
    @GetMapping("/chatroom/list")
    public List<ChatroomDto> getChatroomList(@CookieValue("LTK") String loginToken) {

        JwtPayloadDto payload = jwtService.getPayloadFromToken(loginToken);

        return chatroomService.getChatroomList(Long.parseLong(payload.getUserId()));
    }


    // 채팅방 생성 후 채팅방 ID 반환
    // feign 클라이언트 전용
    @PostMapping("/chatroom")
    public ResponseEntity<String> createChatroom(@RequestBody Map<String, String> request) {

        // userId와 postId가 모두 포함된 경우에만 채팅방 생성
        if (request.containsKey("userId") && request.containsKey("postId")) {
            return new ResponseEntity<>(
                    chatroomService.createChatroom(request.get("userId"),
                                                    Long.parseLong(request.get("postId"))),
                    HttpStatus.CREATED
            );
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    // 채팅방 사용자 추가
    // feign 클라이언트 전용
    @PostMapping("/chatroom/join")
    public ResponseEntity<String> joinChatroom(@RequestBody Map<String, String> request) {

        // chatroomId와 userId가 유효한 경우에만 사용자 추가
        if (request.containsKey("chatroomId") && request.containsKey("userId")) {
            chatroomService.joinChatroom(request.get("chatroomId"),
                    request.get("userId"));
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}