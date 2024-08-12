package com.example.gamemate.domain.friend.repository;

import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FriendRepositoryCustom {
    List<Friend> findFriendsByStatus(Long userId, Friend.Status status);
    List<User> findFriendUsersByUserId(Long userId, Friend.Status status);
    Optional<Friend> findFriendRelationship(Long requesterId, Long receiverId);
    List<Friend> findPendingRequestsByReceiverId(Long receiverId);
    List<Friend> findPendingRequestsByRequesterId(Long requesterId);
}
