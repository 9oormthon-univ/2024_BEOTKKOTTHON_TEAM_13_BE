package com.team13.n1.service;

import com.team13.n1.entity.UserJoinedChats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserJoinedChatsService {
    private final MongoTemplate template;

    // 유저가 참여한 채팅방이 있는지에 대한 여부
    public boolean existsByUserId(String userId) {
        return template.exists(Query.query(Criteria.where("_id").is(userId)),
                UserJoinedChats.class, "user_joined_chats");
    }

    // 유저가 참여한 채팅방들의 id값 반환
    public List<String> getChatsIdsByUserId(String userId) {
        return Objects.requireNonNull(template.findById(userId, UserJoinedChats.class,
                "user_joined_chats")).getChatIds();
    }

    // 새로운 참여 채팅방 추가
    public void addChatIdInUser(String userId, String chatId) {
        // 만약 유저 ID가 이미 존재하는 경우 채팅방 ID를 chat_ids에 추가하고, 만약 유저 ID가 없다면 새로운 데이터 추가
        if (existsByUserId(userId)) {
            UserJoinedChats userJoinedChats = template.findById(userId, UserJoinedChats.class,
                    "user_joined_chats");
            if (userJoinedChats != null) {
                List<String> chatIds = userJoinedChats.getChatIds();
                chatIds.add(chatId);
                template.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                        Update.update("chat_ids", chatIds), UserJoinedChats.class);
            }
        } else {
            List<String> chatIds = new ArrayList<>();
            chatIds.add(chatId);
            save(new UserJoinedChats(userId, chatIds));
        }
    }

    // 유저 퇴장
    public void userExit(String userId, String chatId) {
        UserJoinedChats userJoinedChats = template.findById(userId, UserJoinedChats.class, "user_joined_chats");
        if (userJoinedChats != null) {
            List<String> chatIds = userJoinedChats.getChatIds();
            chatIds.remove(chatId);

            if (chatIds.isEmpty()) {
                template.remove(Query.query(Criteria.where("_id").is(userId)), "user_joined_chats");
                return;
            }

            template.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                    Update.update("chat_ids", chatIds), UserJoinedChats.class);
        }
    }

    public void save(UserJoinedChats userJoinedChats) {
        template.save(userJoinedChats, "user_joined_chats");
    }
}
