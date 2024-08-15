package com.team13.servicechat.service;

import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.repository.ChatMessageRepository;
import com.team13.servicechat.repository.ChatroomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    // 채팅방 메시지 데이터를 저장하는 레포지토리
    private final ChatroomRepository chatroomRepository;

    // 채팅방 메시지를 저장하기 위한 레포지토리
    private final ChatMessageRepository chatMessageRepository;


    @PostConstruct
    private void init() {
        // 테스트 전용 채팅방이 존재하지 않는 경우, 채팅방 생성
        if (!chatroomRepository.existsById("test-chatroom")) {
            chatroomRepository.save(Chatroom.builder()
                            .id("test-chatroom")
                            .postId(0)
                            .messageIds(new ArrayList<>())
                            .userIds(new ArrayList<>(List.of(1L, 2L)))
                            .build());
        }
    }


    // 채팅방 ID가 존재하는지 확인
    public boolean existsChatroomId(String chatroomId) {
        return chatroomRepository.existsById(chatroomId);
    }


    // 채팅방 정보 가져오기
    // 해당 메서드 실행 전에 항상 채팅방 ID exists 여부 확인
    public Chatroom getChatroomById(String id) {
        return chatroomRepository.findById(id).orElseThrow();
    }


    // 채팅 메시지 가져오기
    public Optional<ChatMessage> getMessageById(Long id) {
        return chatMessageRepository.findById(id);
    }


    // 채팅 메시지 저장 및 반환
    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

}
