package com.example.gamemate.domain.post.service;

import com.example.gamemate.domain.post.dto.PostCommentResponseDTO;
import com.example.gamemate.domain.post.dto.PostCommentsResponseDTO;
import com.example.gamemate.domain.post.dto.RecommentsResponseDTO;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.dto.PostCommentDTO;
import com.example.gamemate.domain.post.mapper.PostCommentMapper;
import com.example.gamemate.domain.post.repository.PostCommentRepository;
import com.example.gamemate.domain.post.repository.PostRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.global.exception.CommonExceptionCode;
import com.example.gamemate.global.exception.PostExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    //댓글 리스트 조회
    public CustomPage<PostCommentsResponseDTO> readPostComments(Long id, Pageable pageable){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        // 부모 댓글 필터링해서 가져오기
        List<PostComment> parentComments = post.getPostComments().stream()
                .filter(postComment -> postComment.getParentComment() == null)
                .collect(Collectors.toList());

        // 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), parentComments.size());
        List<PostComment> filteredComments = parentComments.subList(start, end);

        // 댓글 DTO로 변환
        List<PostCommentsResponseDTO> commentsDTO = filteredComments.stream()
                .map(postComment -> PostCommentsResponseDTO.builder()
                        .id(postComment.getId())
                        .userId(postComment.getUser().getId())
                        .username(postCommentRepository.findUsernameByCommentId(postComment.getId()))
                        .nickname(postCommentRepository.findNicknameByCommentId(postComment.getId()))
                        .userProfile(postComment.getUserProfile())
                        .content(postComment.getContent())
                        .recomments(getRecomments(postComment))
                        .createdDate(postComment.getCreatedDate())
                        .modifiedDate(postComment.getModifiedDate())
                        .deletedDate(postComment.getDeletedDate())
                        .build())
                .collect(Collectors.toList());

        CustomPage<PostCommentsResponseDTO> customComments =
                new CustomPage<>(new PageImpl<>(commentsDTO, pageable, parentComments.size()));


        // 댓글 DTO로 변환
        return customComments;
    }

    //게시글 대댓글 조회
    private List<RecommentsResponseDTO> getRecomments(PostComment parentComment){
        return parentComment.getReComments().stream()
                .map(recomment -> RecommentsResponseDTO.builder()
                        .id(recomment.getId())
                        .userId(recomment.getUser().getId())
                        .username(postCommentRepository.findUsernameByCommentId(recomment.getId()))
                        .nickname(postCommentRepository.findNicknameByCommentId(recomment.getId()))
                        .parentCommentId(parentComment.getId())
                        .userProfile(recomment.getUserProfile())
                        .content(recomment.getContent())
                        .createdDate(recomment.getCreatedDate())
                        .modifiedDate(recomment.getModifiedDate())
                        .deletedDate(recomment.getDeletedDate())
                        .build())
                .collect(Collectors.toList());
    }

    //게시글 댓글 생성
    public PostCommentResponseDTO createPostComment(String username, Long id, PostCommentDTO postCommentDTO){

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        User user = userRepository.findByUsername(username);

        //대댓글일 경우 실행되는 로직
        PostComment parentComment = null;
        if(postCommentDTO.getParentCommentId() != null){
            System.out.println(postCommentDTO.getParentCommentId());
            parentComment = postCommentRepository.findById(postCommentDTO.getParentCommentId())
                    .orElseThrow(() -> new RestApiException(PostExceptionCode.COMMENT_NOT_FOUND));
        }
        ;

        PostComment postComment = PostComment.builder()
                .user(user)
                .post(post)
                .parentComment(parentComment)
                .nickname(user.getNickname())
                .userProfile(user.getUserProfile())
                .content(postCommentDTO.getContent())
                .build();

        postCommentRepository.save(postComment);

        PostCommentResponseDTO postCommentResponseDTO = mapper.postCommentToPostCommentResponseDTO(postComment);
        postCommentResponseDTO.setCommentUsername(username);

        return postCommentResponseDTO;
    }

    //게시글 댓글 수정
    public PostCommentResponseDTO updateComment(String username, Long postId, Long commentId, PostCommentDTO postCommentDTO){

        //게시글이 존재하지 않는 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.COMMENT_NOT_FOUND));

        //유저가 맞지 않는 경우
        if(!postComment.getUser().getUsername().equals(username))
            throw new RestApiException(CommonExceptionCode.USER_NOT_MATCH);

        PostCommentResponseDTO postCommentResponseDTO = postCommentRepository.findById(commentId)
                .map(existingPostComment -> {
                    existingPostComment.updateComment(postCommentDTO.getContent());

                    PostComment updatedPostComment = postCommentRepository.save(existingPostComment);

                    return mapper.postCommentToPostCommentResponseDTO(updatedPostComment);
                })
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        postCommentResponseDTO.setCommentUsername(username);

        return postCommentResponseDTO;
    }

    //게시글 댓글 삭제
    public void deleteComment(String username, Long postId, Long commentId){

        //게시글이 존재하지 않는 경우
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        //댓글이 존재하지 않는 경우
        PostComment postComment = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.COMMENT_NOT_FOUND));

        //유저가 맞지 않는 경우
        if(!postComment.getUser().getUsername().equals(username))
            throw new RestApiException(CommonExceptionCode.USER_NOT_MATCH);


        //대댓글의 원댓글 id 받아오기
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

                        //원댓글은 softDelete
                        existingPostComment.deleteComment(LocalDateTime.now(), "삭제된 댓글입니다.");
                        postCommentRepository.save(existingPostComment);
                    });
        }
    }
}
