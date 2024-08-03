package com.example.gamemate.domain.post.service;

import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.dto.PostCommentDTO;
import com.example.gamemate.domain.post.mapper.PostCommentMapper;
import com.example.gamemate.domain.post.mapper.PostMapper;
import com.example.gamemate.domain.post.repository.PostCommentRepository;
import com.example.gamemate.domain.post.repository.PostRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.PostExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostCommentService {

    private final UserRepository userRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostCommentMapper mapper;

    public PostCommentService(UserRepository userRepository, PostCommentRepository postCommentRepository, PostRepository postRepository, PostCommentMapper mapper) {
        this.userRepository = userRepository;
        this.postCommentRepository = postCommentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    //게시글 댓글 생성
    public void createPostComment(String username, Long id, PostCommentDTO postCommentDTO){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        User user = userRepository.findByUsername(username);

        //대댓글일 경우 실행되는 로직
        PostComment parentComment = null;
        if(postCommentDTO.getPCommentId() != null){
            System.out.println(postCommentDTO.getPCommentId());
            parentComment = postCommentRepository.findById(postCommentDTO.getPCommentId())
                    .orElseThrow(() -> new RestApiException(PostExceptionCode.COMMENT_NOT_FOUND));
        }
        ;

        PostComment postComment = PostComment.builder()
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .nickname(user.getNickname())
                .content(postCommentDTO.getContent())
                .build();

        postCommentRepository.save(postComment);
    }

    //게시글 댓글 수정
    public void updateComment(Long postId, Long commentId, PostCommentDTO postCommentDTO){

        postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        postCommentRepository.findById(commentId)
                .map(existingPostComment -> {
                    existingPostComment.updateComment(postCommentDTO.getContent());

                    PostComment updatedPostComment = postCommentRepository.save(existingPostComment);
                    return mapper.postCommentToPostCommentResponseDTO(updatedPostComment);
                })
                .orElseThrow(() -> new IllegalStateException("PostComment with id " + commentId + "does note exist"));
    }

    //게시글 댓글 삭제
    public void deleteComment(Long postId, Long commentId){
        postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.COMMENT_NOT_FOUND));

        Long pCommentId = postCommentRepository.getPCommentId(commentId);

        /*
        대댓글 삭제
         */
        if(postCommentRepository.isRecomment(commentId)){

            //대댓글의 원댓글이 삭제된 상태이고, 삭제할 대댓글이 마지막 대댓글인 경우
            if(postCommentRepository.hasRecomments(pCommentId)== 1
                    && postCommentRepository.isParentCommentDeleted(pCommentId)){

                //대댓글, 원댓글 모두 삭제
                postCommentRepository.deleteById(commentId);
                postCommentRepository.deleteById(pCommentId);
            }else {
                //대댓글만 삭제
                postCommentRepository.deleteById(commentId);
            }

         /*
           대댓글 삭제
          */
        }else if(postCommentRepository.hasRecomments(commentId) == 0){
            postCommentRepository.deleteById(commentId);
        }else{
            //대댓글이 존재하는 경우 삭제된 메시지로 표시
            postCommentRepository.findById(commentId)
                    .ifPresent(existingPostComment -> {

                        existingPostComment.deleteComment(LocalDateTime.now(), "삭제된 댓글입니다.");
                        postCommentRepository.save(existingPostComment);
                    });
        }
    }
}
