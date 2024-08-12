package com.team13.servicechat.service;

import com.team13.servicechat.dto.ChatroomDto;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.repository.ChatroomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    private final ChatroomRepository repository;  // 채팅방 메시지 데이터를 저장하는 레포지토리

    @PostConstruct
    private void init() {
        // 테스트 전용 채팅방이 존재하지 않는 경우, 채팅방 생성
        if (!repository.existsById("test-chatroom")) {
            repository.save(Chatroom.builder()
                            .id("test-chatroom")
                            .postId(0)
                            .messageIds(new ArrayList<>())
                            .userIds(new ArrayList<>(List.of(1L, 2L)))
                            .build());
        }
    }


    // 채팅방 ID가 존재하는지 확인
    public boolean existsById(String id) {
        return repository.existsById(id);
    }


    // 채팅방 ID로 Chatroom 객체 반환
    public ChatroomDto getChatroomById(String id) {
        Chatroom chatroom = repository.findById(id).orElseThrow();

        return ChatroomDto.builder()
                .postId(chatroom.getPostId())
                .userIds(chatroom.getUserIds())
                .messageIds(chatroom.getMessageIds())
                .build();
    }

}
