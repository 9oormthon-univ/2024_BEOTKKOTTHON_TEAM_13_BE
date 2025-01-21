package com.team13.serviceuser.service;

import com.team13.serviceuser.apiPyaload.code.status.ErrorStatus;
import com.team13.serviceuser.apiPyaload.exception.handler.TempHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService{

    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1)
            throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}
