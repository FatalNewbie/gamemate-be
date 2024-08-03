package com.example.gamemate.domain.chat.service;


import com.example.gamemate.domain.chat.mapper.ChatRoomMapper;
import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomCreateResponse createChatRoom(String chatTitle, String leader){

        ChatRoom chatRoom = new ChatRoom(chatTitle, leader);

        // 채팅방 생성
        ChatRoom newChatRoom = chatRoomRepository.save(chatRoom);

        // 생성요청을 보낸 유저를 채팅방멤버의 방장으로 추가함.
        chatRoomMemberService.addMember(newChatRoom.getId(), leader, true);

        return ChatRoomCreateResponse.from(true, "채팅방을 생성하였습니다.");
    }

    public List<ChatRoomDTO> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();
        return chatRooms.stream()
                .map(chatRoomMapper::convertoToChatRoomDTO)
                .collect(Collectors.toList());
    }

    public boolean deleteChatRoom(Long roomId){
        if(chatRoomRepository.existsById(roomId)){
            chatRoomRepository.deleteById(roomId);
            return !chatRoomRepository.existsById(roomId);
        }
        else {
            return false;
        }
    }
}
