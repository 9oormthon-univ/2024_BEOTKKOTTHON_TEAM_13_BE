package com.team13.servicechat.service;

import com.team13.servicechat.dto.ChatMessageDto;
import com.team13.servicechat.dto.ChatroomDto;
import com.team13.servicechat.dto.PostInfoDTO;
import com.team13.servicechat.dto.feign.PostDTO;
import com.team13.servicechat.entity.ChatMessage;
import com.team13.servicechat.entity.Chatroom;
import com.team13.servicechat.entity.UserJoinedChats;
import com.team13.servicechat.feign.PostServiceClient;
import com.team13.servicechat.repository.ChatMessageRepository;
import com.team13.servicechat.repository.ChatroomRepository;
import com.team13.servicechat.repository.UserJoinedChatsRepository;
import feign.FeignException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatroomService {

    // NOTE: service-post에 연결하기 위한 Feign 갹채
    private final PostServiceClient postServiceClient;

    // 채팅방 메시지 데이터를 저장하는 레포지토리
    private final ChatroomRepository chatroomRepository;

    // 채팅방 메시지를 저장하기 위한 레포지토리
    private final ChatMessageRepository chatMessageRepository;

    // 사용자가 속한 채팅방 리스트를 관리하는 레포지토리
    private final UserJoinedChatsRepository userJoinedChatsRepository;

    // 사용자가 읽지 않은 메시지 정보를 관리하는 서비스
    private final UserUnreadMsgsService userUnreadMsgsService;

    // NOTE: 사용자 채팅 세션을 관리하는 서비스
    private final ChatSessionService chatSessionService;


    @PostConstruct
    private void init() {
        // 테스트 전용 채팅방이 존재하지 않는 경우, 채팅방 생성
        if (!chatroomRepository.existsById("test-chatroom")) {
            chatroomRepository.save(Chatroom.builder()
                            .id("test-chatroom")
                            .postId(1)
                            .messageIds(new ArrayList<>())
                            .userIds(new ArrayList<>())
                            .build());
        }
    }


    // 채팅방 생성 후 채팅방 ID 반환
    public String createChatroom(String userId, Long postId) {

        // 공동구매 개설 메시지 저장
        ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                .type("NOTICE")
                .message("채팅방이 열렸습니다.")
                .build());

        // 채팅방 정보 저장
        Chatroom savedChatroom = chatroomRepository.save(Chatroom.builder()
                        .messageIds(new ArrayList<>())
                        .userIds(new ArrayList<>(List.of(Long.parseLong(userId))))
                        .postId(postId)
                        .messageIds(List.of(message.getId()))
                        .lastMessage(message.getMessage())
                        .build());

        // 해당 유저가 참여중인 채팅방 목록 생성
        UserJoinedChats userJoinedChats = UserJoinedChats.builder()
                .id(Long.parseLong(userId))
                .chatroomIds(new ArrayList<>())
                .build();

        // 만약 이미 사용자 정보가 저장되어 있다면 해당 정보로 치환함
        if (userJoinedChatsRepository.existsById(Long.parseLong(userId))) {
            userJoinedChats = userJoinedChatsRepository.findById(Long.parseLong(userId)).orElseThrow();
        }

        // 생성된 채팅방 추가 후 변경내용 저장
        userJoinedChats.addChatroomId(savedChatroom.getId());
        userJoinedChatsRepository.save(userJoinedChats);

        return savedChatroom.getId();
    }


    // 채팅방 사용자 추가
    public void joinChatroom(String chatroomId, String userId) {
        if (existsChatroomId(chatroomId)) {
            // 공동구매 참여 메시지 저장
            ChatMessage message = chatMessageRepository.save(ChatMessage.builder()
                    .type("NOTICE")
                    .message("새로운 유저가 공동구매에 참여하였습니다.")
                    .build());

            Chatroom chatroom = getChatroomById(chatroomId);

            // 메시지 및 사용자 추가 후 변경내용 저장
            chatroom.addMessageId(message.getId());
            chatroom.setLastMessage(message.getMessage());
            chatroom.addUserId(Long.parseLong(userId));
            chatroomRepository.save(chatroom);

            // 해당 유저가 참여중인 채팅방 목록 생성
            UserJoinedChats userJoinedChats = UserJoinedChats.builder()
                    .id(Long.parseLong(userId))
                    .chatroomIds(new ArrayList<>())
                    .build();

            // 만약 이미 사용자 정보가 저장되어 있다면 해당 정보로 치환함
            if (userJoinedChatsRepository.existsById(Long.parseLong(userId))) {
                userJoinedChats = userJoinedChatsRepository.findById(Long.parseLong(userId)).orElseThrow();
            }

            // 생성된 채팅방 추가 후 변경내용 저장
            userJoinedChats.addChatroomId(chatroomId);
            userJoinedChatsRepository.save(userJoinedChats);
        }
    }


    // 채팅방 ID가 존재하는지 확인
    public boolean existsChatroomId(String chatroomId) {
        return chatroomRepository.existsById(chatroomId);
    }


    // 채팅방 ID에 해당 유저가 속하는지 확인
    // 입력되는 chatroomId는 존재하는 채팅방 ID이어야 함
    public boolean verifyUserInChatroom(String userId, String chatroomId) {

        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();

        // 해당 유저 ID가 채팅방에 존재하는지 확인
        return chatroom.getUserIds().contains(Long.parseLong(userId));
    }


    // 채팅방 정보 가져오기
    // 해당 메서드 실행 전에 항상 채팅방 ID exists 여부 확인
    public Chatroom getChatroomById(String chatroomId) {
        return chatroomRepository.findById(chatroomId).orElseThrow();
    }


    // 채팅방 Dto 불러오기 (채팅방 메시지 포함)
    // 입력되는 chatroomId는 존재하는 채팅방 ID이어야 함
    public ChatroomDto getChatroomInfoById(String chatroomId, Long userId) {

        Chatroom chatroom = chatroomRepository.findById(chatroomId).orElseThrow();

        // 채팅방 정보 내에 저장된 메시지 ID 리스트로 메시지 내용을 불러옴
        List<ChatMessageDto> messages = new ArrayList<>();

        // 메시지를 순회하며 존재하는 메시지만 messages에 추가
        for (long messageId : chatroom.getMessageIds()) {
            Optional<ChatMessage> opMessage = getMessageById(messageId);

            opMessage.ifPresent(message -> messages.add(ChatMessageDto.builder()
                    .type(ChatMessageDto.MessageType.valueOf(message.getType()))
                    .message(message.getMessage())
                    .senderUserId(message.getSenderUserId())
                    .senderUserName(message.getSenderUserName())
                    .createdAt(message.getCreatedAt())
                    .build()));
        }

        // 현재 채탕방에서 사용자가 읽지 않은 메시지 삭제
        userUnreadMsgsService.removeUnreadMessagesInChatroom(userId, chatroomId);

        // NOTE: 새로운 사용자 채팅 세션 반환
        String chat_token = chatSessionService.addChatSession(userId);

        return ChatroomDto.builder()
                .id(chatroom.getId())
                .chat_token(chat_token)
                .postId(chatroom.getPostId())
                .userId(userId)
                .userIds(chatroom.getUserIds())
                .messages(messages)
                .build();
    }


    // 사용자가 속한 채팅방 리스트 반환
    public List<ChatroomDto> getChatroomList(Long userId) {

        Optional<UserJoinedChats> userJoinedChats = userJoinedChatsRepository.findById(userId);

        // 사용자가 참여한 채팅방이 존재하는 경우에만 결과 반환
        if (userJoinedChats.isPresent()) {
            List<ChatroomDto> chatroomDtos = new ArrayList<>();

            // 채팅방 ID를 추출하여 chatroomDtos에 채팅방 정보 추가
            for (String chatroomId : userJoinedChats.get().getChatroomIds()) {

                // 채팅방 정보 가져오기
                Optional<Chatroom> opChatroom = chatroomRepository.findById(chatroomId);

                // 해당 유저가 채팅방에서 읽지 않은 메시지들을 불러옴
                List<ChatMessage> unreadMessages = userUnreadMsgsService.getUnreadMsgsInChatroom(userId, chatroomId);

                // 존재하는 채팅방인 경우에만 chatroomDtos에 추가
                if (opChatroom.isPresent()) {
                    // NOTE: 채팅방에 연결된 게시글이 없을 경우를 대비한 try문
                    try {
                        // NOTE: 해당 채팅방의 게시글 정보를 가져옴
                        PostDTO post = postServiceClient.getPostById(opChatroom.get().getPostId());

                        // NOTE: 게시글 정보를 담는 DTO 생성
                        PostInfoDTO postInfo = PostInfoDTO.builder()
                                .title(post.getTitle())
                                .imagePath(post.getImages().isEmpty() ? "" : post.getImages().get(0).getImagePath())
                                .build();

                        // NOTE: 마지막 채팅 메시지 정보 가져오기
                        List<Long> messages = opChatroom.get().getMessageIds();
                        Long lastMsgId = messages.get(messages.size() - 1);

                        Optional<ChatMessage> lastMsg = getMessageById(lastMsgId);

                        // NOTE: 채팅방 정보 추가
                        opChatroom.ifPresent(chatroom -> chatroomDtos.add(ChatroomDto.builder()
                                .id(chatroom.getId())
                                .postId(chatroom.getPostId())
                                .unreadMsgsCounter(unreadMessages.size())
                                .lastMessage(lastMsg.orElse(null))
                                .post(postInfo)
                                .build()));
                    } catch (FeignException e) {
                        log.error("Couldn't find post!", e);
                    }
                }
            }

            return chatroomDtos;
        }

        return new ArrayList<>();
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
