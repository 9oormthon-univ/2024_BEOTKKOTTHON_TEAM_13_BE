package com.team13.n1.service;

import com.team13.n1.entity.ChatBrief;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBriefService {
    private final MongoTemplate template;

    // 채팅방 ID로 객체 데이터 불러오기
    public ChatBrief getById(String chatId) {
        return template.findById(chatId, ChatBrief.class, "chat_brief");
    }

    // 채팅방이 존재하는지 확인
    public boolean existsById(String chatId) {
        return template.exists(Query.query(Criteria.where("_id").is(chatId)),
                ChatBrief.class, "chat_brief");
    }

    // 해당 채팅방의 마지막 메시지 변경
    public void updateLastMessage(String chatId, String message) {
        if (existsById(chatId)) {
            ChatBrief chatBrief = template.findById(chatId, ChatBrief.class, "chat_brief");
            if (chatBrief != null) {
                template.updateFirst(Query.query(Criteria.where("_id").is(chatId)),
                        Update.update("last_message", message), ChatBrief.class);
            }
        }
    }

    // 데이터 저장
    public void save(ChatBrief chatBrief) {
        template.save(chatBrief, "chat_brief");
    }
}
