package com.example.hobbyproject.repository;

import com.example.hobbyproject.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  // 특정 ID 리스트에 해당하는 게시글 모두 삭제(데이터가 있을지 없을지 모르니 Optional)
  Optional<List<Post>> findByIdIn(List<Long> idList);

  // 글 작성 시 동일 제목, 내용 방지(중복 제거)
  boolean existsByTitleOrContents(String title, String contents);




}
