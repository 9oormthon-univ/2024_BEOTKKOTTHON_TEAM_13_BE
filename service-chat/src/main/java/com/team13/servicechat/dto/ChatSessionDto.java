package com.team13.servicechat.dto;

import com.team13.servicechat.entity.ChatSessions;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatSessionDto {
    private String token;
    private String usersId;
    private String usersName;
    private String usersProfile;

    // NOTE: ChatSessions(Entity) -> ChatSessionDto로 변환
    public static ChatSessionDto ofChatSessions(ChatSessions session) {
        return ChatSessionDto.builder()
                .token(session.getToken())
                .usersId(session.getUsers_id())
                .usersName(session.getUsers_name())
                .usersProfile(session.getUsers_profile())
                .build();
    }
}
