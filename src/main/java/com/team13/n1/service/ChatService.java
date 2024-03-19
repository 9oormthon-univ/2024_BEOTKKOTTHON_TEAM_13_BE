package com.team13.n1.service;

import com.team13.n1.dto.MessageDto;
import com.team13.n1.entity.Chat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final MongoTemplate template;

    // 채팅방에 유저 ID 추가
    public void addUserIdInChat(String chatId, String userId) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null) {
            List<String> userIds = chat.getUserIds();
            userIds.add(userId);
            template.updateFirst(Query.query(Criteria.where("_id").is(chatId)),
                    Update.update("user_ids", userIds), Chat.class);
        }
    }

    // 채팅방에 userId를 가진 유저가 있는지 확인
    public boolean existsUserIdInChat(String chatId, String userId) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null) {
            for (String id : chat.getUserIds()) {
                if (id.equals(userId))
                    return true;
            }
        }
        return false;
    }

    // 채팅방 전체 메시지 반환
    public List<MessageDto> getMessagesById(String chatId) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        List<MessageDto> messages = new ArrayList<>();
        if (chat != null) {
            messages.addAll(chat.getMessageDtos());
        }
        return messages;
    }

    // 채팅방 메시지 추가
    public void addMessage(String chatId, MessageDto message) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null) {
            List<MessageDto> messages = chat.getMessageDtos();
            messages.add(message);
            template.updateFirst(Query.query(Criteria.where("_id").is(chatId)),
                    Update.update("messages", messages), Chat.class);
        }
    }

    // 채팅방 내의 모든 유저 ID 반환
    public List<String> getUserIdsInChat(String chatId) {
        List<String> result = new ArrayList<>();
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null)
            result.addAll(chat.getUserIds());
        return result;
    }

    // 유저 퇴장 (채팅방에 해당 유저의 ID를 제거)
    public void userExit(String userId, String chatId) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null) {
            List<String> userIds = chat.getUserIds();
            userIds.remove(userId);

            if (userIds.isEmpty()) {
                template.remove(Query.query(Criteria.where("_id").is(chatId)), "chat");
            }

            template.updateFirst(Query.query(Criteria.where("_id").is(chatId)),
                    Update.update("userIds", userIds), Chat.class);
        }
    }

    // 공동구매 종료 (해당 채팅방 제거)
    public void complete(String chatId) {
        Chat chat = template.findById(chatId, Chat.class, "chat");
        if (chat != null) {
            template.remove(Query.query(Criteria.where("_id").is(chatId)), "chat");
        }
    }

    // 새 채팅방 저장
    public void save(Chat chat) {
        template.save(chat, "chat");
    }

    // 해당 유저의 ID가 포함된 채팅방 생성
    public String save(String userId) {
        String chatId = UUID.randomUUID().toString();

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);

        save(new Chat(chatId, "", userIds, new ArrayList<>()));
        return chatId;
    }
}
