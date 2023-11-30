package com.example.hobbyproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionType {

    ALREADY_DELETED("이미 삭제된 글입니다.", HttpStatus.OK),
    DUPLICATE_POST("이미 존재하는 제목 또는 내용입니다.", HttpStatus.CONFLICT),
    POST_NOT_FOUND("조회할 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
