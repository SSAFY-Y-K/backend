package com.ssafy.passit.common.exception;

import com.ssafy.passit.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        log.warn("API exception occurred. code={}, status={}, message={}",
            errorCode.getCode(), errorCode.getHttpStatus(), exception.getMessage());

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.error(errorCode.getCode(), exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        log.error("Unhandled exception occurred.", exception);

        return ResponseEntity
            .status(errorCode.getHttpStatus())
            .body(ApiResponse.error(errorCode.getCode(), errorCode.getMessage()));
    }
}
