package com.team13.servicerecipe.apiPayload.code.status;

import com.team13.servicerecipe.apiPayload.code.BaseCode;
import com.team13.servicerecipe.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    _OK(HttpStatus.OK, "COMMON200", "요청이 성공적으로 처리되었습니다."),
    RECIPE_CREATED(HttpStatus.CREATED, "RECIPE201", "레시피가 성공적으로 등록되었습니다."),
    COMMENT_CREATED(HttpStatus.CREATED, "COMMENT201", "댓글이 성공적으로 등록되었습니다.");

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
                .build();
    }
}
