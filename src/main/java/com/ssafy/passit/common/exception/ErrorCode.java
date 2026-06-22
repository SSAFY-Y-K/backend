package com.ssafy.passit.common.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "?섎せ???붿껌?낅땲??"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "You do not have permission to perform this action."),
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "PROBLEM_NOT_FOUND", "臾몄젣瑜?李얠쓣 ???놁뒿?덈떎."),
    SUBMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "SUBMISSION_NOT_FOUND", "?쒖텧 ?뺣낫瑜?李얠쓣 ???놁뒿?덈떎."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_NOT_FOUND", "寃뚯떆湲??李얠쓣 ???놁뒿?덈떎."),
    UNSUPPORTED_LANGUAGE(HttpStatus.BAD_REQUEST, "UNSUPPORTED_LANGUAGE", "吏?먰븯吏 ?딅뒗 ?몄뼱?낅땲??"),
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "NOT_IMPLEMENTED", "?꾩쭅 援ы쁽?섏? ?딆? 湲곕뒫?낅땲??"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "?쒕쾭 ?대? ?ㅻ쪟媛 諛쒖깮?덉뒿?덈떎."),
    AI_SERVER_ERROR(HttpStatus.BAD_GATEWAY, "AI_SERVER_ERROR", "AI ?쒕쾭 ?몄텧???ㅽ뙣?덉뒿?덈떎.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
