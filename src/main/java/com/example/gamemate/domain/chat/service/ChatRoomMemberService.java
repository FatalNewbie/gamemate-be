package com.example.gamemate.domain.chat.service;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.domain.ChatRoomMember;


import com.example.gamemate.domain.chat.model.chatroommember.AddMemberResponse;
import com.example.gamemate.domain.chat.repository.ChatRoomMemberRepository;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomRepository chatRoomRepository;

    public AddMemberResponse addMember(Long chatRoomId, String member, boolean isLeader) {
        // 이미 리더가 존재하는 방에 리더 멤버가 추가되는 경우가 발생할때의 예외 처리

        //채팅방 서비스에서 chatRoomId로 채팅 객체 찾아오기.
        //예외처리
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);

        ChatRoomMember chatRoomMember = new ChatRoomMember(chatRoom, member, isLeader);

        chatRoomMemberRepository.save(chatRoomMember);

        return AddMemberResponse.from(true, "멤버를 추가하였습니다.");


    }
}
