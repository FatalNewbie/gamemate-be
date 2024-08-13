package com.example.gamemate.domain.chat.service;


import com.example.gamemate.domain.chat.mapper.ChatRoomMapper;
import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.post.dto.PostUpdateResponseDTO;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.repository.PostRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.ChatExceptionCode;
import com.example.gamemate.global.exception.ChatRoomException;
import com.example.gamemate.global.exception.PostExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomMapper chatRoomMapper;
    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public List<ChatRoomDTO> getAllChatRoomsByUser(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser(user);

        return chatRooms.stream()
                .map(chatRoomMapper::convertoToChatRoomDTO)
                .collect(Collectors.toList());
    }

    public Long getChatRoomByPostId(Long postId){
        Post post = postRepository.findById(postId).orElse(null);
        return chatRoomRepository.findByPost(post).getId();
    }

    public void deleteChatRoom(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new ChatRoomException(ChatExceptionCode.CHATROOM_NOT_FOUND));

        chatRoomRepository.deleteById(roomId);

    }

    public Long getChatRoomMemberCnt(Long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()
                -> new ChatRoomException(ChatExceptionCode.CHATROOM_NOT_FOUND));

        Long chatRoomMemberCnt = chatRoomRepository.countByChatRoom(chatRoom);

        //채팅룸에 해당하는 포스트 id로 게시글을 찾고 모집된 멤버 업데이트
        postRepository.findById(chatRoom.getPost().getId())
                .ifPresentOrElse(existingPost -> {
                    existingPost.updateMemberCnt(chatRoomMemberCnt);
                    postRepository.save(existingPost);
                }, () -> {
                    throw new RestApiException(PostExceptionCode.POST_NOT_FOUND);
                });

        return chatRoomMemberCnt;

    }

}
