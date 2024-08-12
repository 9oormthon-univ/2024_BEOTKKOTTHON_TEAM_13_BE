package com.team13.servicechat.service;

import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;

    // 특정 채팅방의 메시지 목록 가져오기
    public List<ChatMessage> getAllMessagesByChatroomId(String chatroomId) {
        return List.of();
    }

    // 채팅방 메시지 저장
    public ChatMessage saveMessage(ChatMessage message) {
        return repository.save(message);
    }

}
