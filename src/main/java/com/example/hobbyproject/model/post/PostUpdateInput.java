package com.example.hobbyproject.model.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostUpdateInput {

  private String title;
  private String contents;
  private String writer;

}
