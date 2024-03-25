package com.team13.n1.controller;

import com.team13.n1.dto.MessageDto;
import com.team13.n1.entity.ChatBrief;
import com.team13.n1.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 채팅방 컨트롤러
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final UserSessionService sessService;             // 유저 세션 관리 서비스
    private final ChatService chatService;                    // 채팅 관리 서비스
    private final ChatWSService chatWSService;                // 채팅 웹소켓 관리 서비스
    private final UserJoinedChatsService joinedChatsService;  // 참여 채팅방 관리 서비스
    private final ChatBriefService chatBriefService;          // 채팅방 목록에서 보여지는 간단 채팅방 목록
    private final PostService postService;                    // 게시글 관리 서비스

    // 과거 채팅 메시지들 전송
    @GetMapping("/init-messages")
    public Map<String, Object> getInitMessages(@RequestParam("id") String chatId, @RequestParam("session_id") String sessionId) {
        Map<String, Object> hashmap = new HashMap<>();

        if (sessService.existsById(sessionId)) { // 세션이 유효한지 확인
            // 채팅방 정보 저장
            int postId = chatService.getPostIdById(chatId);
            hashmap.put("post_title", postService.getTitleById(postId)); // 해당 채팅방과 연결된 게시글의 제목 저장

            // messages 리스트 내에 메시지들을 담아서 저장
            List<MessageDto> messages = new ArrayList<>();
            String userId = sessService.getUserIdBySessionId(sessionId);
            if (chatService.existsUserIdInChat(chatId, userId)) {
                messages.addAll(chatService.getMessagesById(chatId));
            }
            hashmap.put("messages", messages);
        }

        return hashmap;
    }

    // 새로운 채팅방 생성
    @PostMapping
    public String createNewChat(@RequestBody Map<String, String> request) {
        if (sessService.existsById(request.get("session_id"))) {
            String userId = sessService.getUserIdBySessionId(request.get("session_id"));
            return chatService.save(userId, 1);
        }
        return "";
    }

    // 채팅방 참여 (해당 채팅방에 유저 ID 등록)
    @PostMapping("join")
    public void joinChat(@RequestBody Map<String, String> request) {
        if (sessService.existsById(request.get("session_id"))) {
            String userId = sessService.getUserIdBySessionId(request.get("session_id"));
            String chatId = request.get("chat_id");
            // 만약 해당 채팅방에 유저가 참여하고 있지 않은 경우에만 참여가능
            if (!chatService.existsUserIdInChat(chatId, userId)) {
                chatWSService.join(chatId, userId, userId); // 해당 유저 입장 메시지('~유저가 입장하였습니다.') 표시
                chatService.addUserIdInChat(request.get("chat_id"), userId); // 유저 ID 등록
                joinedChatsService.addChatIdInUser(userId, chatId);
            }
        }
    }

    // 채팅방 퇴장 (거래 종료에 의한 퇴장이 아닌, 유저 자발적인 퇴장)
    @PostMapping("exit-user")
    public void exitUser(@RequestBody Map<String, String> request) {
        if (sessService.existsById(request.get("session_id"))) {
            String userId = sessService.getUserIdBySessionId(request.get("session_id"));
            String chatId = request.get("chat_id");
            if (chatService.existsUserIdInChat(chatId, userId)) {
                chatService.userExit(userId, chatId); // 채팅방 데이터에서 해당 유저 ID 제거
                chatWSService.userExit(request.get("session_id"), userId, chatId); // 웹소켓에서도 해당 유저 제거
                joinedChatsService.userExit(userId, chatId); // 유저가 참여한 채팅방에서 해당 채팅방 제거
            }
        }
    }
}
