package com.example.gamemate.domain.chat.service;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.entity.ChatRoomMember;


import com.example.gamemate.domain.chat.model.chatroommember.AddMemberResponse;
import com.example.gamemate.domain.chat.repository.ChatRoomMemberRepository;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.ChatExceptionCode;
import com.example.gamemate.global.exception.ChatRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public AddMemberResponse addMember(Long roomId,
                                       Long targetMemberId,
                                       boolean isLeader) {
        // 이미 리더가 존재하는 방에 리더 멤버가 추가되는 경우가 발생할때의 예외 처리


        //채팅방 서비스에서 chatRoomId로 채팅 객체 찾아오기.
        //예외처리
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatRoomException(ChatExceptionCode.CHATROOM_NOT_FOUND));

        User member = userRepository.findById(targetMemberId).orElse(null);


        ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoom, member, isLeader);
        chatRoomMemberRepository.save(chatRoomMember);

        return AddMemberResponse.from(true, "멤버를 추가하였습니다.");


    }

    public void deleteMember(Long roomId, UserDetails userDetails){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new ChatRoomException(ChatExceptionCode.CHATROOM_NOT_FOUND));
        User user = userRepository.findByUsername(userDetails.getUsername());

        ChatRoomMember chatRoomMember= chatRoomMemberRepository.findByChatRoomAndMember(chatRoom,user);
        chatRoomMemberRepository.deleteById(chatRoomMember.getId());
    }

}
