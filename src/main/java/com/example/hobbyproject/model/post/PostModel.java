package com.example.hobbyproject.model.post;

import com.example.hobbyproject.entity.Post;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
//클라이언트로 전송할 때는 클라이언트에서 필요로 하는 형태로 데이터를 변환하는데 이때 DTO 또는 Model을 사용
//클라이언트에게 보여주어야 할 정보(삭제 정보 X)
public class PostModel {

  private long id;
  private String title;
  private String contents;
  private String writer;
  private LocalDateTime regDate;
  private LocalDateTime updateDate;
  private Integer hits;
  private Integer likes;

  // Post 엔터티에서 PostModel로 변환하는 메서드, 엔티티와 모델 간의 변환
  public static PostModel fromEntity(Post post) {
    return PostModel.builder()
        .id(post.getId())
        .title(post.getTitle())
        .contents(post.getContents())
        .writer(post.getWriter())
        .regDate(post.getRegDate())
        .updateDate(post.getUpdateDate())
        .hits(post.getHits())
        .likes(post.getLikes())
        .build();
  }
}