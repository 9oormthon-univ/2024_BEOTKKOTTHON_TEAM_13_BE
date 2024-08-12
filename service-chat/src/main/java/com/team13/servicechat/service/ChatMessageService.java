package com.team13.servicechat.service;

import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;  // 채팅방 메시지를 저장하기 위한 레포지토리


    // 특정 ID를 가진 채팅 메시지 가져오기
    public Optional<ChatMessage> getMessageById(Long id) {
        return repository.findById(id);
    }

    // 채팅방 메시지 저장
    public ChatMessage saveMessage(ChatMessage message) {
        return repository.save(message);
    }

}
