package com.example.hobbyproject.service;

import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.entity.PostLike;
import com.example.hobbyproject.entity.User;
import com.example.hobbyproject.exception.PostException;
import com.example.hobbyproject.exception.UserException;
import com.example.hobbyproject.model.post.PostModel;
import com.example.hobbyproject.repository.PostLikeRepository;
import com.example.hobbyproject.repository.PostRepository;
import com.example.hobbyproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.hobbyproject.type.PostExceptionType.*;
import static com.example.hobbyproject.type.UserExceptionType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    // 좋아요 API(특정아이디로)
    @Transactional
    public PostModel likePost(Long userId, Long postId) throws PostException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        // 이미 좋아요를 눌렀는지 확인
        if (postLikeRepository.existsByUserAndPost(user, post)) {
            throw new PostException(ALREADY_LIKED);
        }

        // 좋아요 엔티티 생성 및 저장
        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(user);
        postLike.setLikeDate(LocalDateTime.now());

        postLikeRepository.save(postLike);

        // 게시글의 좋아요 수 증가
        post.setLikes(post.getLikes() + 1);
        postRepository.save(post);

        return PostModel.fromEntity(post);
    }

    // 좋아요 취소 API
    @Transactional
    public PostModel unlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));

        // 좋아요를 눌렀는지 확인
        PostLike postLike = postLikeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new PostException(NOT_LIKED));

        // 좋아요 엔티티 삭제
        postLikeRepository.delete(postLike);

        // 게시글의 좋아요 수 감소
        post.setLikes(post.getLikes() - 1);

        return PostModel.fromEntity(post);
    }

    // 좋아요 순서대로 게시글 보는 API
    public List<Post> getPopularPosts() {
        return postRepository.findAllByOrderByLikesDesc();
    }

}
