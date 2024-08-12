package com.example.gamemate.domain.chat.repository;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c JOIN c.memberList m WHERE m.member = :user")
    List<ChatRoom> findAllByUser(@Param("user") User user);

    @Query("SELECT COUNT(c) FROM ChatRoomMember c WHERE c.chatRoom = :chatRoom")
    long countByChatRoom(@Param("chatRoom") ChatRoom chatRoom);

    ChatRoom findByPost(@Param("Post") Post post);
}
