package com.team13.serviceuser.service;

import com.team13.serviceuser.converter.SignConverter;
import com.team13.serviceuser.dto.SignRequest;
import com.team13.serviceuser.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public boolean checkEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    //닉네임 중복확인
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    //비밀번호 암호화

    public void registerUser(SignRequest.RegisterRequestDto registerRequestDto) {
        userRepository.save(SignConverter.toUserEntity(registerRequestDto, encoder.encode(registerRequestDto.getPassword())));
    }
}
