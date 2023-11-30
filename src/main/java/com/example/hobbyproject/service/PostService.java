package com.example.hobbyproject.service;

import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.exception.PostException;
import com.example.hobbyproject.exception.PostExceptionType;
import com.example.hobbyproject.model.PostCreateInput;
import com.example.hobbyproject.model.PostDeleteInput;
import com.example.hobbyproject.model.PostModel;
import com.example.hobbyproject.model.PostUpdateInput;
import com.example.hobbyproject.repository.PostRepository;

import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
// PostRepository를 주입받아서 게시글 조회 및 작성과 관련된 로직을 처리
public class PostService {

    private final PostRepository postRepository;

    // 게시글 전체 조회
    public List<PostModel> getAllPosts() {
        // 전체 게시글을 조회하고 PostModel로 변환하여 반환
        return postRepository.findAll().stream()
                .map(PostModel::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 게시글 조회
    public PostModel getPostById(Long id) {
        // 특정 ID의 게시글을 조회하고 PostModel로 변환하여 반환
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));
        return PostModel.fromEntity(post);
    }

    // 게시글 조회 시 조회수 증가
    @Transactional
    public PostModel incrementHits(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        post.setHits(post.getHits() + 1);

        Post updatedPost = postRepository.save(post);

        return PostModel.fromEntity(updatedPost);
    }

    // 게시글 작성
    public PostModel createPost(PostCreateInput postCreateInput) {

        // 글 작성 시 동일 제목, 내용 방지(중복 제거)
        if (postRepository.existsByTitleOrContents(postCreateInput.getTitle(), postCreateInput.getContents())) {
            // 중복된 경우 처리 (예: 예외 발생)
            throw new PostException(PostExceptionType.DUPLICATE_POST);
        }

        // hits와 likes는 자동으로 0으로 초기화됨
        Post post = Post.builder()
                .title(postCreateInput.getTitle())
                .contents(postCreateInput.getContents())
                .writer(postCreateInput.getWriter())
                .regDate(LocalDateTime.now())
                .build();

        // 생성된 게시글을 PostRepository를 통해 저장
        Post savedPost = postRepository.save(post);

        // 저장된 게시글 엔터티를 PostModel로 변환하여 반환
        return PostModel.fromEntity(savedPost);
    }

    // 게시글 수정
    public PostModel updatePost(Long id, PostUpdateInput updateInput) {
        // 게시글 id 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        // 이미 생성된 객체의 필드를 업데이트할 때는 필드를 직접 설정하는 것이 편리함(빌더 패턴 X)
        post.setTitle(updateInput.getTitle());
        post.setContents(updateInput.getContents());
        post.setWriter(updateInput.getWriter());
        post.setUpdateDate(LocalDateTime.now());

        return PostModel.fromEntity(postRepository.save(post));
    }


    // 게시글 선택 삭제(플래그)
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        if (post.isDeleted()) {
            throw new  PostException(PostExceptionType.ALREADY_DELETED);
        }

        post.setDeleted(true);
        post.setDeletedDate(LocalDateTime.now());

        // 저장하여 상태 업데이트
        postRepository.save(post);
    }

    // 게시글 전체 삭제(플래그)
    @Transactional
    public void deletePosts(PostDeleteInput deleteInput) {
        List<Post> postList = postRepository.findByIdIn(deleteInput.getIdList())
                .orElseThrow(() -> new PostException(PostExceptionType.POST_NOT_FOUND));

        postList.forEach(e -> {
            e.setDeleted(true);
            e.setDeletedDate(LocalDateTime.now());
        });

        postRepository.saveAll(postList);
    }

    // 게시글 전체 삭제(물리+논리) : 이미 데이터 분석에 사용을 했거나, 고객 정보 보호 기한이 지났을 때 물리적으로 삭제할 필요성
    public void deleteAllPostsPhysically() {
        postRepository.deleteAll();
    }

    // 최근 게시글 n개 목록 반환
    public Page<PostModel> getLatestPosts(int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.Direction.DESC, "regDate");
        return postRepository.findAll(pageable).map(PostModel::fromEntity); //PostModel 클래스의 fromEntity)
    }
}
