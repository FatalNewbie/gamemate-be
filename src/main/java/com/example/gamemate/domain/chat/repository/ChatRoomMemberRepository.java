package com.example.gamemate.domain.chat.repository;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.entity.ChatRoomMember;
import com.example.gamemate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    void deleteByChatRoomAndMember(ChatRoom chatRoom, User member);

    ChatRoomMember findByChatRoomAndMember(ChatRoom chatRoom, User member);

}
