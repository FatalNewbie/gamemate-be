package com.example.gamemate.domain.post.service;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.chat.service.ChatRoomMemberService;
import com.example.gamemate.domain.chat.service.ChatRoomService;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.dto.*;
import com.example.gamemate.domain.post.mapper.PostMapper;
import com.example.gamemate.domain.post.repository.PostCommentRepository;
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
    //Status 값에 따라서 다른 로직 처리
    @Transactional
    public PostResponseDTO createPost(String username, PostDTO postDTO) {

        User user = userRepository.findByUsername(username);

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(postDTO.getGameTitle(), user, postDTO.getMateCnt()));

        chatRoomMemberService.addMember(chatRoom.getId(), username, true);

        if ("ON".equals(postDTO.getStatus())) {

            Post post = handleOnlinePost(user, (OnlinePostDTO) postDTO);

            PostResponseDTO postResponseDTO = mapper.PostToPostResponse(post);

            postResponseDTO.setPostUsername(username);

            return postResponseDTO;

        } else if ("OFF".equals(postDTO.getStatus())) {

            Post post = handleOfflinePost(user, (OfflinePostDTO) postDTO);

            PostResponseDTO postResponseDTO = mapper.PostToPostResponse(post);

            postResponseDTO.setPostUsername(username);

            return postResponseDTO;
        } else {
            throw new RestApiException(CommonExceptionCode.INVALID_PARAMETER);
        }
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
                                postUpdateDTO.getMateCnt(),
                                postUpdateDTO.getMateContent());
                    }
                    if (postUpdateDTO.getStatus().equals("OFF")){
                        existingPost.updateOfflinePost(
                                postUpdateDTO.getMateCnt(),
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


    // 온라인 포스트 처리 로직
    private Post handleOnlinePost(User user, OnlinePostDTO onlinePostRequest) {

        Post post = Post.builder()
                .gameTitle(onlinePostRequest.getGameTitle())
                .gameGenre(onlinePostRequest.getGameGenre())
                .status(Post.OnOffStatus.ON)
                .user(user)
                .nickname(user.getNickname())
                .mateCnt(onlinePostRequest.getMateCnt())
                .mateContent(onlinePostRequest.getMateContent())
                .build();

        return postRepository.save(post);
    }

    //오프라인 포스트 처리 로직
    private Post handleOfflinePost(User user, OfflinePostDTO offlinePostDTO) {

        Post post = Post.builder()
                .gameTitle(offlinePostDTO.getGameTitle())
                .gameGenre(offlinePostDTO.getGameGenre())
                .status(Post.OnOffStatus.OFF)
                .user(user)
                .nickname(user.getNickname())
                .mateCnt(offlinePostDTO.getMateCnt())
                .mateContent(offlinePostDTO.getMateContent())
                .mateRegionSi(offlinePostDTO.getMateRegionSi())
                .mateRegionGu(offlinePostDTO.getMateRegionGu())
                .mateLocation(offlinePostDTO.getMateLocation())
                .latitude(offlinePostDTO.getLatitude())
                .longitude(offlinePostDTO.getLongitude())
                .build();

        return postRepository.save(post);
    }

}