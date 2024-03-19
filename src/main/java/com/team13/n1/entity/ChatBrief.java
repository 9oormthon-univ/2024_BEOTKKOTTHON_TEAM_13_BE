package com.team13.n1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "chat_brief")
@AllArgsConstructor
public class ChatBrief {
    @Id private String chatId;
    @Field("post_id") private String postId;            // 채팅방과 연결된 글 ID
    @Field("last_message") private String lastMessage;  // 해당 채팅방의 마지막 메시지
}
