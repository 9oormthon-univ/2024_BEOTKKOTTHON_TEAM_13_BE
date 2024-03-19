package com.team13.n1.service;

import com.team13.n1.entity.ChatBrief;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBriefService {
    private final MongoTemplate template;

    // 채팅방 ID로 객체 데이터 불러오기
    public ChatBrief getById(String id) {
        return template.findById(id, ChatBrief.class, "chat_brief");
    }

    // 데이터 저장
    public void save(ChatBrief chatBrief) {
        template.save(chatBrief, "chat_brief");
    }
}
