package com.example.hobbyproject.repository;

import com.example.hobbyproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    //내용 중복 체크
    boolean existsByContents(String contents);

}
