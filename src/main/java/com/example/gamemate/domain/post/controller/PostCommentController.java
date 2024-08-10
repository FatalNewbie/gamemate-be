package com.example.gamemate.domain.post.controller;

import com.example.gamemate.domain.post.dto.PostCommentDTO;
import com.example.gamemate.domain.post.dto.PostCommentResponseDTO;
import com.example.gamemate.domain.post.dto.PostCommentsResponseDTO;
import com.example.gamemate.domain.post.service.PostCommentService;
import com.example.gamemate.global.apiRes.ApiResponse;
import com.example.gamemate.global.common.CustomPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post Comment", description = "Post Comment API")
@RequestMapping("/post")
@RestController
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @GetMapping("/{id}/comment")
    public ApiResponse<CustomPage<PostCommentsResponseDTO>> getAllComments(
            @PathVariable(name = "id") Long id,
            @PageableDefault(size = 10)Pageable pageable){

        CustomPage<PostCommentsResponseDTO> postCommentsResponseDTO =
                postCommentService.readPostComments(id, pageable);

        return ApiResponse.successRes(HttpStatus.OK, postCommentsResponseDTO);
    }

    //게시글 댓글 작성 api
    @PostMapping("/{id}/comment")
    public ApiResponse<PostCommentResponseDTO> registerPostComment(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody PostCommentDTO postCommentDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        PostCommentResponseDTO postCommentResponseDTO =
                postCommentService.createPostComment(userDetails.getUsername(), id, postCommentDTO);

        return ApiResponse.successRes(HttpStatus.CREATED,postCommentResponseDTO);
    }

    //게시글 댓글 수정 api
    @PutMapping("/{postId}/comment/{commentId}")
    public ApiResponse<Object> editComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody PostCommentDTO postCommentDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        PostCommentResponseDTO postCommentResponseDTO =
                postCommentService.updateComment(userDetails.getUsername(), postId, commentId, postCommentDTO);

        return ApiResponse.successRes(HttpStatus.OK,postCommentResponseDTO);
    }


    //게시글 댓글 삭제 api
    @DeleteMapping("/{postId}/comment/{commentId}")
    public ApiResponse<Object> removeComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        postCommentService.deleteComment(userDetails.getUsername(), postId, commentId);

        return ApiResponse.successRes(HttpStatus.NO_CONTENT,commentId);
    }

}