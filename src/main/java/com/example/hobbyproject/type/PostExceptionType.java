package com.example.hobbyproject.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostExceptionType {

    ALREADY_DELETED("이미 삭제된 글입니다.", HttpStatus.OK),
    DUPLICATE_POST("이미 존재하는 제목 또는 내용입니다.", HttpStatus.CONFLICT),
    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_LIKED("이미 좋아요를 누른 게시물입니다.", HttpStatus.BAD_REQUEST),
    NOT_LIKED("좋아요를 누르지 않은 게시물 입니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
