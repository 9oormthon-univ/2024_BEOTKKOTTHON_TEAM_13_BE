package com.team13.servicechat.service;

import com.team13.servicechat.dto.ChatMessageDto;
import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.entity.UserJoinedChats;
import com.team13.servicechat.repository.ChatMessageRepository;
import com.team13.servicechat.repository.ChatroomRepository;
import com.team13.servicechat.repository.UserJoinedChatRepository;
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

    // 사용자가 속한 채팅방 리스트를 관리하는 레포지토리
    private final UserJoinedChatRepository userJoinedChatRepository;


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


    // 채팅방 생성 후 채팅방 ID 반환
    public String createChatroom(String userId, Long postId) {
        Chatroom savedChatroom = chatroomRepository.save(Chatroom.builder()
                        .messageIds(new ArrayList<>())
                        .lastMessage("")
                        .userIds(new ArrayList<>(List.of(Long.parseLong(userId))))
                        .postId(postId)
                        .build());

        // 해당 유저가 참여중인 채팅방 목록 생성
        UserJoinedChats userJoinedChats = UserJoinedChats.builder()
                .id(Long.parseLong(userId))
                .chatroomIds(new ArrayList<>())
                .build();

        // 만약 이미 사용자 정보가 저장되어 있다면 해당 정보로 치환함
        if (userJoinedChatRepository.existsById(Long.parseLong(userId))) {
            userJoinedChats = userJoinedChatRepository.findById(Long.parseLong(userId)).orElseThrow();
        }

        // 생성된 채팅방 추가 후 변경내용 저장
        userJoinedChats.addChatroomId(savedChatroom.getId());
        userJoinedChatRepository.save(userJoinedChats);

        return savedChatroom.getId();
    }


    // 채팅방 사용자 추가
    public void joinChatroom(String chatroomId, String userId) {
        if (existsChatroomId(chatroomId)) {
            Chatroom chatroom = getChatroomById(chatroomId);

            // 사용자 추가 후 변경내용 저장
            chatroom.addUserId(Long.parseLong(userId));
            chatroomRepository.save(chatroom);

            // 해당 유저가 참여중인 채팅방 목록 생성
            UserJoinedChats userJoinedChats = UserJoinedChats.builder()
                    .id(Long.parseLong(userId))
                    .chatroomIds(new ArrayList<>())
                    .build();

            // 만약 이미 사용자 정보가 저장되어 있다면 해당 정보로 치환함
            if (userJoinedChatRepository.existsById(Long.parseLong(userId))) {
                userJoinedChats = userJoinedChatRepository.findById(Long.parseLong(userId)).orElseThrow();
            }

            // 생성된 채팅방 추가 후 변경내용 저장
            userJoinedChats.addChatroomId(chatroomId);
            userJoinedChatRepository.save(userJoinedChats);
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
    public void saveMessage(String chatroomId, ChatMessageDto message) {
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
