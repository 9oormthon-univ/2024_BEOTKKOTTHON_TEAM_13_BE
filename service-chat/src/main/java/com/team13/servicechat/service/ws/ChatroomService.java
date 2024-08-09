package com.team13.servicechat.service.ws;

import com.team13.servicechat.dto.ChatroomMessage;
import com.team13.servicechat.entity.ChatroomMessages;
import com.team13.servicechat.entity.Chatrooms;
import com.team13.servicechat.repository.ChatroomsRepository;
import com.team13.servicechat.service.ChatroomMessagesService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j2
@Service
public class ChatroomService {

    // 채팅방 메시지 데이터를 저장하는 레포지토리
    private final ChatroomsRepository repository;

    // 채팅방 목록 (repository와의 잦은 통신을 줄이기 위한 캐싱을 담당함)
    private final HashSet<String> chatroomIds;

    public ChatroomService(ChatroomsRepository repository) {
        this.repository = repository;

        chatroomIds = new HashSet<>();
    }

    // 채팅방 입장
    public Optional<ChatroomMessage> messageEnter(String chatroomId, ChatroomMessage message) {
        // 존재하는 채팅방인 경우 메시지 발신자를 해당 채팅방에 입장시킴
        if (verifyChatroomId(chatroomId)) {
            // DB에 해당 유저 정보 추가
            Chatrooms chatroom = repository.findById(chatroomId).orElseThrow();

            chatroom.addUserId(Long.parseLong(message.getSenderUserId()));
            repository.save(chatroom);

            return Optional.of(ChatroomMessage.builder()
                            .type(ChatroomMessage.MessageType.NOTICE)
                            .message(message.getSenderUserName() + "님이 입장하셨습니다.")
                            .build());
        }

        return Optional.empty();
    }

    // 채팅 메시지
    public Optional<ChatroomMessage> messageTextAndImage(String chatroomId, ChatroomMessage message) {
        // 채팅방이 존재하는 경우에만 메시지를 반환함(반환한다는 의미는 이 메시지를 DB에 저장한다는 의미임)
        if (verifyChatroomId(chatroomId)) {
            return Optional.of(message);
        }

        return Optional.empty();
    }

    // 채팅방에서 유저 퇴장
    public Optional<ChatroomMessage> messageExitUser(String chatroomId, ChatroomMessage message) {
        // 나가려는 채팅방이 존재하는지 확인
        if (verifyChatroomId(chatroomId)) {
            // 채팅방 데이터를 가져옴
            Chatrooms chatroom = repository.findById(chatroomId).orElseThrow();

            // 해당 채팅방에서 유저가 포함되어 있는지 확인
            int userIndex = chatroom.getUsersIds().indexOf(Long.valueOf(message.getSenderUserId()));

            if (userIndex != -1) {
                // 퇴장 유저 정보를 채팅방 내에서 제거
                chatroom.getUsersIds().remove(userIndex);

                // 만약 모든 유저가 채팅방에서 나간 경우, 해당 채팅방 제거
                if (chatroom.getUsersIds().isEmpty()) {
                    repository.deleteById(chatroomId);
                    chatroomIds.remove(chatroomId);

                    // TODO: feign을 통한 다른 서비스들에게도 채팅방 삭제 메시지 전달

                    return Optional.empty();
                }

                return Optional.of(ChatroomMessage.builder()
                                .type(ChatroomMessage.MessageType.NOTICE)
                                .message(message.getSenderUserName() + "님이 퇴장하셨습니다.")
                                .build());
            }
        }

        return Optional.empty();
    }

    // TODO: 공동구매 완료
    public Optional<ChatroomMessage> messageComplete(String chatroomId, ChatroomMessage message) {
       /*
       공동구매 완료 기능은 공동구매 게시자만 요청할 수 있으며, 게시자가 완료 요청시 해당 메시지를 전체 클라이언트에게 전송합니다.
       해당 메시지를 받은 다른 사용자들은 리뷰를 작성하면 messageExitUser() 메서드를 통해 채팅방을 나갈 수 있으며, 모든 유저가
       채팅방을 나간 경우 해당 채팅방 정보를 chatrooms과 DB 상에서 제거한다.
        */

        return Optional.empty();
    }

    // 채팅방에 해당 메시지 아이디 추가
    public void addMessageIdInChatroom(String chatroomId, ChatroomMessages message) {
        if (verifyChatroomId(chatroomId)) {
            Chatrooms chatroom = repository.findById(chatroomId).orElseThrow();

            // 해당 메시지를 저장한 뒤, 채팅방의 마지막 메시지를 해당 메시지로 설정
            chatroom.getMessageIds().add(message.getId());
            chatroom.setLastMessage(message.getMessage());

            repository.save(chatroom);
        }
    }

    // 존재하는 채팅방인지 확인
    // 가장 먼저, 캐쉬에 저장된 채팅방 ID인지 확인한 후 캐쉬에 없으면, DB를 확인하여 존재하는 아이디인지 확인
    private boolean verifyChatroomId(String chatroomId) {
        log.info(repository.findAll());

        if (chatroomIds.contains(chatroomId)) {
            // 해당 채팅방의 ID가 캐시 채팅방 목록에 저장되어 있는 경우
            return true;

        } else if (repository.existsById(chatroomId)) {
            // 채팅방의 ID가 캐시 채팅방에는 없으나 DB에 있는 경우 해당 아이디를 캐쉬에 저장
            chatroomIds.add(chatroomId);
            return true;
        }

        // 두 저장소 모두 해당 아이디가 없는 경우 false 반환
        return false;
    }

    // TODO: 채팅방 내에 존재하는 유저인지 확인
    // verifyChatroomId와 마찬가지로 캐시에서 유저 ID를 확인한 뒤, DB를 확인하여 유저 검증 진행
    private boolean verifyUserIdInChatroom(String chatroomId, String userId) {
        return false;
    }

}
