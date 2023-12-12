package com.example.hobbyproject.repository;

import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.entity.PostLike;
import com.example.hobbyproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    //내가 쓴 좋아요, 특정 사용자가 좋아요한 게시물 목록 조회
    List<PostLike> findByUser(User user);

    //좋아요
    boolean existsByUserAndPost(User user, Post post);

    //좋아요 취소
    Optional<PostLike> findByUserAndPost(User user, Post post);

}
