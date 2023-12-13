package com.example.hobbyproject.model.comment;

import com.example.hobbyproject.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentUserInput {

    private Long id;

    private String email;

    private String contents;

    public static CommentUserInput fromCommentEntity(Comment comment) {
        return CommentUserInput.builder()
                .id(comment.getUser().getId())
                .email(comment.getUser().getEmail())
                .contents(comment.getContents())
                .build();
    }

}
