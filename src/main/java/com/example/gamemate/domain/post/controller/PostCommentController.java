package com.example.gamemate.domain.post.controller;

import com.example.gamemate.domain.post.dto.PostCommentDTO;
import com.example.gamemate.domain.post.dto.PostCommentResponseDTO;
import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.service.PostCommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post Comment", description = "Post Comment API")
@RequestMapping("/post")
@RestController
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    //게시글 댓글 작성 api
    @PostMapping("/{id}/comment")
    public ResponseEntity<PostCommentResponseDTO> registerPostComment(@PathVariable Long id,
                                                      @Valid @RequestBody PostCommentDTO postCommentDTO,
                                                      @AuthenticationPrincipal UserDetails userDetails){

        PostCommentResponseDTO postCommentResponseDTO =
                postCommentService.createPostComment(userDetails.getUsername(), id, postCommentDTO);

        return ResponseEntity.ok(postCommentResponseDTO);
    }

    //게시글 댓글 수정 api
    @PutMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Object> editComment(@PathVariable Long postId,
                                              @PathVariable Long commentId,
                                              @Valid @RequestBody PostCommentDTO postCommentDTO,
                                              @AuthenticationPrincipal UserDetails userDetails){

        PostCommentResponseDTO postCommentResponseDTO =
                postCommentService.updateComment(userDetails.getUsername(), postId, commentId, postCommentDTO);

        return ResponseEntity.ok(postCommentResponseDTO);
    }


    //게시글 댓글 삭제 api
    @DeleteMapping("/{postId}/comment/{commentId}")
    public ResponseEntity<Object> removeComment(@PathVariable Long postId,
                                                @PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetails userDetails){

        postCommentService.deleteComment(userDetails.getUsername(), postId, commentId);

        return ResponseEntity.noContent().build();
    }

}