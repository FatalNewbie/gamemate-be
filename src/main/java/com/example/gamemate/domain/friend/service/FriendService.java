package com.example.gamemate.domain.friend.service;

import com.example.gamemate.domain.friend.dto.FriendPostDto;
import com.example.gamemate.domain.friend.dto.FriendPutDto;
import com.example.gamemate.domain.friend.dto.FriendResponseDto;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.friend.entity.FriendId;
import com.example.gamemate.domain.friend.repository.FriendRepository;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.InvalidUserIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public FriendResponseDto sendFriendRequest(FriendPostDto friendPostDto) {
        User requester = userRepository.findById(friendPostDto.getRequesterId())
                .orElseThrow(() -> new InvalidUserIdException("Invalid requester ID"));
        User receiver = userRepository.findById(friendPostDto.getReceiverId())
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        Optional<Friend> existingFriend = friendRepository.findFriendRelationship(requester.getId(), receiver.getId());

        if (existingFriend.isPresent()) {
            Friend friend = existingFriend.get();
            if (friend.getStatus() == Friend.Status.ACCEPTED) {
                return new FriendResponseDto("이미 친구인 유저입니다.", Friend.Status.ACCEPTED, requester, receiver);
            } else if (friend.getStatus() == Friend.Status.PENDING) {
                return new FriendResponseDto("친구 요청이 와있는 대상입니다.", Friend.Status.PENDING, requester, receiver);
            }
        }

        Friend friend = new Friend();
        friend.setRequester(requester);
        friend.setReceiver(receiver);
        friend.setStatus(Friend.Status.PENDING);

        friendRepository.save(friend);

        return new FriendResponseDto("친구 요청이 완료되었습니다.", friend.getStatus(), requester, receiver);
    }

    public FriendResponseDto respondToFriendRequest(FriendPutDto friendPutDto) {
        FriendId friendId = new FriendId(friendPutDto.getRequesterId(), friendPutDto.getReceiverId());
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        if (friendPutDto.getStatus() == Friend.Status.ACCEPTED) {
            friend.setStatus(Friend.Status.ACCEPTED);
            friendRepository.save(friend);
            return new FriendResponseDto("친구 요청을 수락하였습니다.", friend.getStatus(), friend.getRequester(), friend.getReceiver());
        } else if (friendPutDto.getStatus() == Friend.Status.REJECTED) {
            friendRepository.delete(friend);
            return new FriendResponseDto("친구 요청을 거절하였습니다.", Friend.Status.REJECTED, friend.getRequester(), friend.getReceiver());
        }

        return new FriendResponseDto("잘못된 요청 상태입니다.", friend.getStatus(), friend.getRequester(), friend.getReceiver());
    }

    public List<User> getFriendsByStatus(Long userId, Friend.Status status) {
        return friendRepository.findFriendUsersByUserId(userId, status);
    }

    public String deleteFriend(Long requesterId, Long receiverId) {
        FriendId friendId = new FriendId(requesterId, receiverId);
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        friendRepository.delete(friend);
        return "친구 삭제가 완료되었습니다.";
    }
}

