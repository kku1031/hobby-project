package com.example.hobbyproject.controller;

import com.example.hobbyproject.entity.Comment;
import com.example.hobbyproject.error.CreateResponseError;
import com.example.hobbyproject.model.comment.CommentRequest;
import com.example.hobbyproject.model.comment.CommentUserInput;
import com.example.hobbyproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/comment")
public class CommentController {

    private final CommentService commentService;

    // 특정 댓글 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentUserInput> getComment(@PathVariable Long commentId) {
        CommentUserInput comment = commentService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    // 특정 게시물의 모든 댓글 조회
    @GetMapping("/all/{postId}")
    public ResponseEntity<List<CommentUserInput>> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentUserInput> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 생성
    @PostMapping("/create")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentRequest commentRequest) {
        CommentUserInput createdComment = commentService.createComment(commentRequest);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 수정
    @PutMapping("update/{commentId}")
    public ResponseEntity<Object> updateComment(@PathVariable Long commentId,
                                                @RequestBody @Valid CommentRequest commentRequest,
                                                Errors errors) {
        // 필수 항목 미입력시 에러 처리(Object는 FieldError 반환)
        if (errors.hasErrors()) {
            List<CreateResponseError> responseErrors = new ArrayList<>();

            errors.getAllErrors().forEach(e -> {
                responseErrors.add(CreateResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        CommentUserInput updatedComment = commentService.updateComment(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("delete/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }
}


