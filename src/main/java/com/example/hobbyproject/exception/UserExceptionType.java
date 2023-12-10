package com.example.hobbyproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType {

    USER_NOT_FOUND("사용자 정보가 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_EMAIL("이미 존재하는 이메일 주소입니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);

    private final String message;
    private final HttpStatus httpStatus;

}
