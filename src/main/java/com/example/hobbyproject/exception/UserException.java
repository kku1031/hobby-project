package com.example.hobbyproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserException extends RuntimeException{

    private final UserExceptionType userExceptionType;

    public UserException(UserExceptionType userExceptionType) {
        super(userExceptionType.getMessage());
        this.userExceptionType = userExceptionType;
    }

    public HttpStatus getHttpStatus() {
        return this.userExceptionType.getHttpStatus();
    }
}
