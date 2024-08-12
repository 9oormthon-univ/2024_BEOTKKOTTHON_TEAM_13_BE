package com.team13.servicechat.service;

import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.repository.ChatroomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
                            .userIds(new ArrayList<>())
                            .build());
        }
    }

}
