package com.ssafy.passit.common.exception;

import com.ssafy.passit.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.error(errorCode.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
