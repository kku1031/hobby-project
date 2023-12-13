package com.example.hobbyproject.service;

import com.example.hobbyproject.entity.Comment;
import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.entity.User;
import com.example.hobbyproject.exception.PostException;
import com.example.hobbyproject.exception.UserException;
import com.example.hobbyproject.model.comment.CommentRequest;
import com.example.hobbyproject.model.comment.CommentUserInput;
import com.example.hobbyproject.repository.CommentRepository;
import com.example.hobbyproject.repository.PostRepository;
import com.example.hobbyproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.hobbyproject.type.PostExceptionType.*;
import static com.example.hobbyproject.type.UserExceptionType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 특정 댓글 조회
    public CommentUserInput getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        return CommentUserInput.fromCommentEntity(comment);
    }

    // 특정 게시물의 모든 댓글 조회
    public List<CommentUserInput> getCommentsByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentUserInput::fromCommentEntity)
                .collect(Collectors.toList());
    }

    // 댓글 생성
    @Transactional
    public CommentUserInput createComment(CommentRequest commentRequest) {
        // 내용이 비어 있는지 체크
        if (Objects.isNull(commentRequest.getContents()) || commentRequest.getContents().trim().isEmpty()) {
            throw new PostException(NOT_EMPTY_COMMENT);
        }

        // 중복 체크: 같은 내용의 댓글이 이미 존재하는지 확인
        if (commentRepository.existsByContents(commentRequest.getContents())) {
            throw new PostException(DUPLICATE_COMMENT);
        }

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        User user = userRepository.findById(commentRequest.getUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .post(post)
                .user(User.builder().id(user.getId()).email(user.getEmail()).build())
                .contents(commentRequest.getContents())
                .regDate(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        // CommentUser로 변환하여 반환
        return CommentUserInput.fromCommentEntity(savedComment);
    }


    // 댓글 수정
    @Transactional
    public CommentUserInput updateComment(Long commentId, CommentRequest commentRequest) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        // 내용이 비어 있거나 공백인지 체크
        if (commentRequest.getContents().isBlank()) {
            throw new PostException(NOT_EMPTY_COMMENT);
        }
        // 중복 체크: 같은 내용의 댓글이 이미 존재하는지 확인
        if (commentRepository.existsByContents(commentRequest.getContents())) {
            throw new PostException(DUPLICATE_COMMENT);
        }

        comment.setContents(commentRequest.getContents());
        comment.setUpdateDate(LocalDateTime.now());

        Comment updatedComment = commentRepository.save(comment);

        return CommentUserInput.fromCommentEntity(updatedComment);
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new PostException(COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }
}
