package com.example.gamemate.domain.chat.repository;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.entity.ChatRoomMember;
import com.example.gamemate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    void deleteByChatRoomAndMember(ChatRoom chatRoom, User member);

    ChatRoomMember findByChatRoomAndMember(ChatRoom chatRoom, User member);


    @Query("SELECT COUNT(c) > 0 FROM ChatRoomMember c WHERE c.chatRoom = :chatRoom AND c.member = :member")
    boolean existsByChatRoomAndMember(@Param("chatRoom") ChatRoom chatRoom, @Param("member") User member);


}
