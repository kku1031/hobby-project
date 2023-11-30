package com.example.hobbyproject.exception;

import lombok.Getter;

@Getter
//비즈니스 로직에 딱맞는 Exception이 있는 경우가 잘 없어서
//보통 customException으로 별도의 exception패키지를 만들어서 관리.

public class PostException extends RuntimeException{

    private final PostExceptionType exceptionType;

    public PostException(PostExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }
}
