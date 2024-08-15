package com.team13.servicechat.service;

import com.team13.servicechat.dto.MessageDto;
import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.repository.ChatMessageRepository;
import com.team13.servicechat.repository.ChatroomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatroomService {

    // 채팅방 메시지 데이터를 저장하는 레포지토리
    private final ChatroomRepository chatroomRepository;

    // 채팅방 메시지를 저장하기 위한 레포지토리
    private final ChatMessageRepository chatMessageRepository;


    @PostConstruct
    private void init() {
        // 테스트 전용 채팅방이 존재하지 않는 경우, 채팅방 생성
        if (!chatroomRepository.existsById("test-chatroom")) {
            chatroomRepository.save(Chatroom.builder()
                            .id("test-chatroom")
                            .postId(0)
                            .messageIds(new ArrayList<>())
                            .userIds(new ArrayList<>(List.of(1L, 2L)))
                            .build());
        }
    }


    // 채팅방 ID가 존재하는지 확인
    public boolean existsChatroomId(String chatroomId) {
        return chatroomRepository.existsById(chatroomId);
    }


    // 채팅방 정보 가져오기
    // 해당 메서드 실행 전에 항상 채팅방 ID exists 여부 확인
    public Chatroom getChatroomById(String chatroomId) {
        return chatroomRepository.findById(chatroomId).orElseThrow();
    }


    // 채팅 메시지 가져오기
    public Optional<ChatMessage> getMessageById(Long messageId) {
        return chatMessageRepository.findById(messageId);
    }


    // 채팅방 및 메시지 삭제
    public void deleteChatroom(String chatroomId) {
        if (existsChatroomId(chatroomId)) {
            Chatroom chatroom = getChatroomById(chatroomId);

            //  채팅방 내의 메시지 삭제
            chatroom.getMessageIds().forEach(chatMessageRepository::deleteById);

            // 채팅방 삭제
            chatroomRepository.deleteById(chatroomId);
        }
    }


    // 채팅방 메시지 추가
    public void saveMessage(String chatroomId, MessageDto message) {
        if (existsChatroomId(chatroomId)) {
            // 채팅방 메시지 저장
            ChatMessage savedMessage = chatMessageRepository.save(ChatMessage.builder()
                            .chatroomId(chatroomId)
                            .type(message.getType().toString())
                            .message(message.getMessage())
                            .senderUserId(message.getSenderUserId())
                            .senderUserName(message.getSenderUserName())
                            .build());

            Chatroom chatroom = getChatroomById(chatroomId);

            // 해당 메시지를 채팅방에 저장한 뒤에 채팅방의 마지막 메시지를 해당 메시지로 설정
            chatroom.getMessageIds().add(savedMessage.getId());
            chatroom.setLastMessage(savedMessage.getMessage());

            // 채팅방 정보 업데이트
            chatroomRepository.save(chatroom);
        }
    }
}
