package com.example.hobbyproject.controller;


import com.example.hobbyproject.error.CreateResponseError;
import com.example.hobbyproject.exception.AlreadyDeletedException;
import com.example.hobbyproject.exception.DuplicatePostException;
import com.example.hobbyproject.exception.PostNotFoundException;
import com.example.hobbyproject.model.PostCreateInput;
import com.example.hobbyproject.model.PostDeleteInput;
import com.example.hobbyproject.model.PostModel;
import com.example.hobbyproject.model.PostUpdateInput;
import com.example.hobbyproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
// 서비스를 주입받아 HTTP 요청을 처리하고 결과를 반환
public class PostController {

    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping
    public ResponseEntity<List<PostModel>> getAllPosts() {
        List<PostModel> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 게시글 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostModel> getPostById(@PathVariable Long id) {
        PostModel post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    // 게시글 작성
    @PostMapping
    public ResponseEntity<Object> createPost(@RequestBody @Valid PostCreateInput postCreateInput, Errors errors) {

        //필수 항목 미입력시 에러 처리(Object는 FieldError 반환)
        if (errors.hasErrors()) {
            List<CreateResponseError> responseErrors = new ArrayList<>();

            errors.getAllErrors().forEach(e -> {
                responseErrors.add(CreateResponseError.of((FieldError) e));
            });
            return new ResponseEntity<>(responseErrors, HttpStatus.BAD_REQUEST);
        }

        PostModel createdPost = postService.createPost(postCreateInput);

        return ResponseEntity.ok(createdPost);
    }

    // 게시글 수정
    @PutMapping("/update/{id}")
    public ResponseEntity<PostModel> updatePost(@PathVariable Long id, @RequestBody PostUpdateInput updateRequest) {
        PostModel updatedPost = postService.updatePost(id, updateRequest);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 조회 시 조회수 증가(부분적 갱신은 Patch 사용 - 특정 게시물 조회와 Mapping 맞춤 - 클릭 때 마다 hits같이 증가)
    @PatchMapping("/{id}")
    public ResponseEntity<PostModel> postHits(@PathVariable Long id) {
        PostModel updatedPost = postService.incrementHits(id);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 선택 삭제(플래그)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

    // 게시글 전체 삭제(플래그)
    @DeleteMapping
    public ResponseEntity<String> deleteAllPosts(@RequestBody PostDeleteInput deleteInput) {
        postService.deletePosts(deleteInput);
        return ResponseEntity.ok("전체 게시글이 삭제되었습니다.");
    }

    // 게시글 전체 삭제(물리 + 논리)
    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllPostsPhysically() {
        postService.deleteAllPostsPhysically();
        return ResponseEntity.ok("전체 게시글이 물리적으로 삭제되었습니다.");
    }

    // 최근 게시글 n개 목록 반환
    @GetMapping("/list/{size}")
    public ResponseEntity<Page<PostModel>> getLatestPosts(@PathVariable int size) {
        Page<PostModel> latestPosts = postService.getLatestPosts(size);
        return ResponseEntity.ok(latestPosts);
    }


}