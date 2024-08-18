package com.team13.servicechat.service;

import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.UserUnreadMessages;
import com.team13.servicechat.repository.ChatMessageRepository;
import com.team13.servicechat.repository.UserUnreadMsgsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserUnreadMsgsService {

    // 유저가 읽지 않은 메시지가 저장된 레포지토리
    private final UserUnreadMsgsRepository userUnreadMsgsRepository;

    // 채팅방 메시지를 저장하는 레포지토리
    private final ChatMessageRepository chatMessageRepository;


    // 특정 유저가 읽지 않은 메시지 반환
    public List<ChatMessage> getUnreadMessages(Long userId) {

        // 사용자가 읽지 않은 메시지가 저장될 리스트
        List<ChatMessage> unreadMessages = new ArrayList<>();

        Optional<UserUnreadMessages> userUnreadMessages = userUnreadMsgsRepository.findById(userId);

        // 만약 존재하지 않은 사용자이거나, 읽지 않은 메시지가 없는 경우 빈 리스트 반환
        if (userUnreadMessages.isEmpty()) {
            return unreadMessages;
        }

        // 읽지 않은 메시지 리스트 추가
        unreadMessages.addAll(userUnreadMessages.get().getUnreadMessages());

        return unreadMessages;
    }


    // 사용자가 읽지 않은 메시지 추가
    public void addUnreadMessage(Long userId, ChatMessage message) {

        // 유저가 읽지 않은 메시지 엔티티를 가져옴
        Optional<UserUnreadMessages> userUnreadMessages = userUnreadMsgsRepository.findById(userId);

        // 만약 이전에 읽지 않은 리스트가 존재하는 경우 해당 리스트에 메시지 추가
        if (userUnreadMessages.isPresent()) {
            // 새로운 메시지를 리스트에 추가함
            userUnreadMessages.get().getUnreadMessages().add(message);

            userUnreadMsgsRepository.save(userUnreadMessages.get());

            log.info(userUnreadMessages);
        } else {
            // 새로운 메시지 리스트 생성
            userUnreadMsgsRepository.save(UserUnreadMessages.builder()
                            .id(userId)
                            .unreadMessages(List.of(message))
                            .build());
        }

    }

}
