package com.team13.serviceuser.dto;

import com.team13.serviceuser.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinRequest {

    @Email(message = "이메일은 유효해야합니다")
    @NotBlank(message = "이메일을 입력하세요")
    private String email;

    @Size(min = 8, max = 16, message = "비밀번호는 최소 8자에서 16자여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호는 각각 최소 1개의 대문자,소문자,숫자,특수기호를 포함해야합니다.")
    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "비밀번호를 다시 입력하세요")
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력하세요")
    private String nickname;
    private float userRating;
    private String profileImageUrl;

    // 비밀번호 암호화
    public User toEntity(String encodedPassword) {
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .nickname(this.nickname)
                .userRating(0L) //초깃값 0L
                .profileImageUrl("profileImage.png") //초깃값 url
                .build();
    }
}
