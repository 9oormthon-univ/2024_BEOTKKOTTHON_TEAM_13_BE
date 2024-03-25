package com.team13.n1.entity;

import com.team13.n1.dto.MessageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "chat")
@AllArgsConstructor
public class Chat {
    @Id private String id;
    @Field("post_id") private int postId;                  // 해당 채팅방과 연결된 글 ID
    @Field("user_ids") private List<String> userIds;          // 해당 채팅방에 참여중인 유저 ID들
    @Field("messages") private List<MessageDto> messageDtos;  // 해당 채팅방 내의 모든 메시지들
}
