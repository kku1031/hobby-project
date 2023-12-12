package com.example.hobbyproject.model.post;

import com.example.hobbyproject.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private long id;

    private long regUserId;
    private String regUserName;

    private String title;
    private String contents;
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private int hits;
    private int likes;

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .writer(post.getWriter())
                .contents(post.getContents())
                .regDate(post.getRegDate())
                .regUserId(post.getUser().getId())
                .regUserName(post.getUser().getUserName())
                .updateDate(post.getUpdateDate())
                .hits(post.getHits())
                .likes(post.getLikes())
                .build();
    }
}
