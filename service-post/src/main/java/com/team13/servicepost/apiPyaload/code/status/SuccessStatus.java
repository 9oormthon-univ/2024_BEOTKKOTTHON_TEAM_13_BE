package com.team13.servicepost.apiPyaload.code.status;

import com.team13.servicepost.apiPyaload.code.BaseCode;
import com.team13.servicepost.apiPyaload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    //post
    POST_CREATED(HttpStatus.CREATED, "POST201", "게시글이 성공적으로 생성되었습니다."),
    POST_FOUND(HttpStatus.OK, "POST200", "게시글이 정상적으로 조회되었습니다."),

    //like
    LIKE_ADDED(HttpStatus.CREATED, "LIKE201", "좋아요가 추가되었습니다."),
    LIKE_REMOVED(HttpStatus.OK, "LIKE202", "좋아요가 취소되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
