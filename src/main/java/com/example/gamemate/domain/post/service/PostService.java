package com.example.gamemate.domain.post.service;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.chat.service.ChatRoomMemberService;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.dto.*;
import com.example.gamemate.domain.post.mapper.PostMapper;
import com.example.gamemate.domain.post.repository.PostRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.common.CustomPage;
import com.example.gamemate.global.exception.CommonExceptionCode;
import com.example.gamemate.global.exception.PostExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {



    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMemberService chatRoomMemberService;
    private final PostMapper mapper;



    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            ChatRoomRepository chatRoomRepository, ChatRoomMemberService chatRoomMemberService,
            PostMapper mapper
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomMemberService = chatRoomMemberService;
        this.mapper = mapper;
    }

    //게시글 리스트 조회
    public CustomPage<PostResponseDTO> readPosts(String status, Pageable pageable){

        Page<Post> postPage = null;

        if(status.toUpperCase().equals("ON")){
            postPage = postRepository.findByStatusOrderByCreatedDateDesc(Post.OnOffStatus.ON, pageable);
        }

        if(status.toUpperCase().equals("OFF")){
            postPage = postRepository.findByStatusOrderByCreatedDateDesc(Post.OnOffStatus.OFF, pageable);
        }

        assert postPage != null;

        return new CustomPage<>(postPage.map(PostResponseDTO::new));
    }

    //게시글 조회
    public PostResponseDTO readPost(String username, Long id){

        // 게시글 조회
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        User user = userRepository.findByUsername(username);

        return PostResponseDTO.builder()
                .id(post.getId())
                .username(post.getUser().getUsername())
                .nickname(post.getUser().getNickname())
                .userProfile(post.getUser().getUserProfile())
                .gameTitle(post.getGameTitle())
                .status(post.getStatus().toString())
                .gameGenre(post.getGameGenre())
                .mateCnt(post.getMateCnt())
                .mateContent(post.getMateContent())
                .commentCnt(postRepository.countCommentsByPostId(post.getId()))
                .mateRegionSi(post.getMateRegionSi())
                .mateRegionGu(post.getMateRegionGu())
                .mateLocation(post.getMateLocation())
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .createdDate(post.getCreatedDate())
                .build();
    }


    //게시글 생성
    @Transactional
    public PostResponseDTO createPost(String username, PostDTO postDTO) {
        User user = userRepository.findByUsername(username);
        Post.OnOffStatus status = "ON".equals(postDTO.getStatus()) ? Post.OnOffStatus.ON : Post.OnOffStatus.OFF;

        Post post = createPost(user, postDTO, status);
        PostResponseDTO postResponseDTO = mapper.PostToPostResponse(post);
        postResponseDTO.setPostUsername(username);

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(postDTO.getGameTitle(), user, postDTO.getMateCnt(), post));
        chatRoomMemberService.addMember(chatRoom.getId(), user.getId(), true);

        return postResponseDTO;
    }

    //게시글 수정
    public PostUpdateResponseDTO updatePost(String username, Long id, PostUpdateDTO postUpdateDTO) {

        //게시글이 존재하지 않는 경우
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));


        //유저가 맞지 않는 경우
        if(!post.getUser().getUsername().equals(username))
            throw new RestApiException(CommonExceptionCode.USER_NOT_MATCH);

        return postRepository.findById(id)
                .map(existingPost -> {
                    if (postUpdateDTO.getStatus().equals("ON")){
                        existingPost.updateOnlinePost(
                                postUpdateDTO.getMateContent());
                    }
                    if (postUpdateDTO.getStatus().equals("OFF")){
                        existingPost.updateOfflinePost(
                                postUpdateDTO.getMateContent(),
                                postUpdateDTO.getMateRegionSi(),
                                postUpdateDTO.getMateRegionGu(),
                                postUpdateDTO.getLatitude(),
                                postUpdateDTO.getLongitude());
                    }
                    Post updatedPost = postRepository.save(existingPost);

                    PostUpdateResponseDTO postUpdateResponseDTO =
                            mapper.PostToPostUpdateResponseDTO(updatedPost);

                    postUpdateResponseDTO.setPostUsername(username);

                    return postUpdateResponseDTO;
                })
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));
    }

    //게시글 삭제
    public void deletePost(String username, Long id){

        //게시글이 존재하지 않는 경우
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RestApiException(PostExceptionCode.POST_NOT_FOUND));

        //유저가 맞지 않는 경우
        if(!post.getUser().getUsername().equals(username))
            throw new RestApiException(CommonExceptionCode.USER_NOT_MATCH);

        postRepository.deleteById(id);
    }


    //포스트 처리하는 로직
    private Post createPost(User user, PostDTO postDTO, Post.OnOffStatus status) {
        Post post = Post.builder()
                .gameTitle(postDTO.getGameTitle())
                .gameGenre(postDTO.getGameGenre())
                .status(status)
                .user(user)
                .nickname(user.getNickname())
                .mateCnt(postDTO.getMateCnt())
                .mateContent(postDTO.getMateContent())
                .build();

        if (postDTO instanceof OfflinePostDTO) {
            OfflinePostDTO offlinePost = (OfflinePostDTO) postDTO;
            post.createOfflinePost(
                    offlinePost.getMateRegionSi(),
                    offlinePost.getMateRegionGu(),
                    offlinePost.getMateLocation(),
                    offlinePost.getLatitude(),
                    offlinePost.getLongitude()
                    );
        }

        return postRepository.save(post);
    }

    //마이페이지에 게시글을 불러오는 로직
    public CustomPage<PostResponseDTO> readPostsByUserId(Long userId, Pageable pageable) {
        Page<Post> postPage = postRepository.findByUserId(userId, pageable); // 사용자 ID에 따라 게시글 조회

        return new CustomPage<>(postPage.map(PostResponseDTO::new)); // PostResponseDTO로 매핑
    }


}