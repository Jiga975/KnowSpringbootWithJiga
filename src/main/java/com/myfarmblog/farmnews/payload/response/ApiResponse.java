package com.myfarmblog.farmnews.payload.response;

import com.myfarmblog.farmnews.utils.DateUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Data
public class ApiResponse<T> {
    private String responseMessage;
    private T responseData;
    private final String responseTime = DateUtils.toDateString(LocalDateTime.now());

    public ApiResponse(String responseMessage, T responseData) {
        this.responseMessage = responseMessage;
        this.responseData = responseData;
    }

    public ApiResponse(String responseMessage) {
        this.responseMessage = responseMessage;
        this.responseData = null;
    }
}
