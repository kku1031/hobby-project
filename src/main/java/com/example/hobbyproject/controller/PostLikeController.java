package com.example.hobbyproject.controller;

import com.example.hobbyproject.entity.Post;
import com.example.hobbyproject.model.post.PostModel;
import com.example.hobbyproject.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 API
    @PostMapping("/{postId}/like/{userId}")
    public ResponseEntity<PostModel> likePost(@PathVariable Long postId,
                                              @PathVariable Long userId) {
        PostModel likedPost = postLikeService.likePost(userId, postId);
        return ResponseEntity.ok(likedPost);
    }

    // 좋아요 취소 API
    @DeleteMapping("/{postId}/like/{userId}")
    public ResponseEntity<PostModel> unlikePost(@PathVariable Long postId,
                                                @PathVariable Long userId) {
        PostModel unlikedPost = postLikeService.unlikePost(userId, postId);
        return ResponseEntity.ok(unlikedPost);
    }

    // 좋아요 순서대로 게시글 보는 API
    @GetMapping("/popular")
    public ResponseEntity<List<Post>> getPopularPosts() {
        List<Post> popularPosts = postLikeService.getPopularPosts();
        return ResponseEntity.ok(popularPosts);
    }
}