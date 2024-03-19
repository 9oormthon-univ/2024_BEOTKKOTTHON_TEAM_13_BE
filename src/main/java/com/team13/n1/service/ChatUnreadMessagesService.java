package com.team13.n1.service;

import com.team13.n1.dto.ChatUnreadMessageDto;
import com.team13.n1.entity.ChatUnreadMessages;
import com.team13.n1.entity.UserJoinedChats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatUnreadMessagesService {
    private final MongoTemplate template;

    // 해당 유저가 읽지 않은 메시지가 있는지 여부 반환
    public boolean existsByUserId(String userId) {
        return template.exists(Query.query(Criteria.where("_id").is(userId)),
                UserJoinedChats.class, "chat_unread_messages");
    }

    // 해당 유저가 읽지 않은 메시지들 반환
    public List<ChatUnreadMessageDto> getUnreadMessagesByUserId(String userId) {
        return Objects.requireNonNull(template.findById(userId, ChatUnreadMessages.class,
                "chat_unread_messages")).getUnreadMessages();
    }

    // 유저가 채팅방 화면 밖에 있는 경우 미확인 메시지로 추가
    public void addMessage(String userId, String chatId, String message) {
        ChatUnreadMessages chatUnreadMessages = template.findById(userId, ChatUnreadMessages.class,
                "chat_unread_messages");
        if (chatUnreadMessages != null) {
            List<ChatUnreadMessageDto> unreadMessages = chatUnreadMessages.getUnreadMessages();
            unreadMessages.add(new ChatUnreadMessageDto(chatId, message));
            template.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                    Update.update("unread_messages", unreadMessages), ChatUnreadMessages.class);
        }
    }

    // 해당 채팅방의 미확인 메시지 삭제 (유저가 해당 채팅방에 다시 돌아온 경우)
    public void removeAllMessagesInChat(String userId, String chatId) {
        ChatUnreadMessages chatUnreadMessages = template.findById(userId, ChatUnreadMessages.class,
                "chat_unread_messages");
        if (chatUnreadMessages != null) {
            List<ChatUnreadMessageDto> unreadMessages = chatUnreadMessages.getUnreadMessages();
            unreadMessages.removeIf(um -> (um.getChatId().equals(chatId)));
            template.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                    Update.update("unread_messages", unreadMessages), ChatUnreadMessages.class);
        }
    }

    // 테스트용으로 작성됨
    public void save(ChatUnreadMessages chatUnreadMessages) {
        template.save(chatUnreadMessages);
    }
}
