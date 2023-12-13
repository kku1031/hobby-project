package com.example.hobbyproject.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommentRequest {

    private Long postId;
    private Long userId;
    private String contents;

}
