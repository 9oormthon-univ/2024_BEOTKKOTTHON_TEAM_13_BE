package com.team13.serviceuser.service;

import com.team13.serviceuser.converter.SignConverter;
import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.entity.User;
import com.team13.serviceuser.feign.ChatServiceClient;
import com.team13.serviceuser.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final ChatServiceClient chatServiceClient;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.default-profile}")
    private String defaultProfilePath;

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복확인
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //비밀번호 암호화

    public void registerUser(SignRequest.RegisterRequestDto registerRequestDto) {
        String fileName = UUID.randomUUID() + ".png";
        String profileImageUrl = "/images/profile/" + fileName;

        try {
            File dest = new File(uploadDir + fileName);
            if (!dest.getParentFile().exists()) dest.getParentFile().mkdirs();

            Files.copy(Paths.get(defaultProfilePath), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("기본 프로필 이미지 복사 실패", e);
        }

        User savedUser = userRepository.save(
                SignConverter.toUserEntity(registerRequestDto, encoder.encode(registerRequestDto.getPassword()), profileImageUrl)
        );
        // TOOD: 개발 단계에서 모든 유저는 테스트 채팅방에 초대함 (추후 테스트 완료시 아래 코드 제거)
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("chatroomId", "test-chatroom");
        requestBody.put("userId", savedUser.getId().toString());
        chatServiceClient.joinChatroom(requestBody);
    }
}
