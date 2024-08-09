package com.team13.servicechat.service;

import com.team13.servicechat.entity.ChatroomMessages;
import com.team13.servicechat.repository.ChatroomMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomMessagesService {

    private final ChatroomMessagesRepository repository;

    // 특정 채팅방의 메시지 목록 가져오기
    public List<ChatroomMessages> getAllMessagesByChatroomId(String chatroomId) {
        return List.of();
    }

    // 채팅방 메시지 저장
    public ChatroomMessages saveMessage(ChatroomMessages message) {
        return repository.save(message);
    }

}
