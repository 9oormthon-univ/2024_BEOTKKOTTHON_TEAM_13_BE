package com.team13.serviceuser.service;

import com.team13.serviceuser.converter.SignConverter;
import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.feign.ChatServiceClient;
import com.team13.serviceuser.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    private ChatServiceClient chatServiceClient;

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복확인
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //비밀번호 암호화

    public void registerUser(SignRequest.RegisterRequestDto registerRequestDto) {
        User savedUser = userRepository.save(SignConverter.toUserEntity(registerRequestDto, encoder.encode(registerRequestDto.getPassword())));

        // TOOD: 개발 단계에서 모든 유저는 테스트 채팅방에 초대함 (추후 테스트 완료시 아래 코드 제거)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("chatroomId", "test-chatroom");
        requestBody.put("userId", savedUser.getId().toString());
        chatServiceClient.joinChatroom(requestBody);
    }
}
