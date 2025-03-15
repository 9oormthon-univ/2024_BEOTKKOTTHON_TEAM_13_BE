package com.team13.servicechat.service;

import com.team13.servicechat.dto.ChatSessionDto;
import com.team13.servicechat.dto.UserDto;
import com.team13.servicechat.entity.ChatSessions;
import com.team13.servicechat.feign.UserServiceClient;
import com.team13.servicechat.repository.ChatSessionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatSessionService {

    // NOTE: 세션의 유저 정보를 가져오기 위한 Feign Client
    private final UserServiceClient userServiceClient;

    // NOTE: 사용자의 채팅 세션을 저장하기 위한 레포지토리
    private final ChatSessionsRepository chatSessionsRepository;

    // NOTE: 세션 TTL (초 단위)
    @Value("${app.session.ttlSeconds}")
    private int ttlSeconds;

    // NOTE: 사용자 채팅 세션 추가
    public String addChatSession(Long userId) {
        // NOTE: 사용자에게 전달될 채팅 세션 토큰
        String token = UUID.randomUUID().toString();

        // NOTE: 유저 서비스에 해당 유저의 정보를 요청
        UserDto user = userServiceClient.getUserById(userId);

        // TODO: 해당 유저의 ID가 없는 경우의 예외 처리 추가

        // NOTE: 새로운 세션 생성
        ChatSessions session = ChatSessions.builder()
                .token(token)
                .users_id(user.getId().toString())
                .users_name(user.getNickname())
                .users_profile(user.getProfileImageUrl())
                .expire_at(new Date(System.currentTimeMillis() + ttlSeconds * 1000L))
                .build();

        chatSessionsRepository.save(session);

        return token;
    }

    // NOTE: 유효한 세션인지 확인
    public boolean isValid(String token) {
        return chatSessionsRepository.existsByToken(token);
    }

    // NOTE: 특정 토큰의 세션으로부터 유저 ID와 이름을 가져옴
    public Optional<ChatSessionDto> getChatSession(String token) {
        if (isValid(token)) {
            Optional<ChatSessions> opSession = chatSessionsRepository.findChatSessionsByToken(token);

            return opSession.map(ChatSessionDto::ofChatSessions);
        }

        return Optional.empty();
    }
}
