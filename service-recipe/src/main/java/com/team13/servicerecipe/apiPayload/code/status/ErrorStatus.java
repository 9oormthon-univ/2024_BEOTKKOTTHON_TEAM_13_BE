package com.team13.servicerecipe.apiPayload.code.status;

import com.team13.servicerecipe.apiPayload.code.BaseErrorCode;
import com.team13.servicerecipe.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RECIPE500", "서버 에러 발생."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "RECIPE400", "잘못된 요청입니다."),
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECIPE404", "레시피를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
