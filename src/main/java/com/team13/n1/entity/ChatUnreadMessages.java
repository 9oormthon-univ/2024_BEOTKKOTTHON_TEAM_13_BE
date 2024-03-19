package com.team13.n1.entity;

import com.team13.n1.dto.ChatUnreadMessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "chat_unread_messages")
@AllArgsConstructor
public class ChatUnreadMessages {
    @Id private String user_id;
    @Field("unread_messages") private List<ChatUnreadMessageDto> unreadMessages;  // 해당 유저가 미확인한 메시지들
}
