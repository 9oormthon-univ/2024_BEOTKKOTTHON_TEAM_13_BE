package com.team13.serviceuser.apiPyaload.exception.handler;

import com.team13.serviceuser.apiPyaload.code.BaseErrorCode;
import com.team13.serviceuser.apiPyaload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}