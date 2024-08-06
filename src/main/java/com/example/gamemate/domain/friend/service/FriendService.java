package com.example.gamemate.domain.friend.service;

import com.example.gamemate.domain.friend.dto.FriendPostDTO;
import com.example.gamemate.domain.friend.dto.FriendPutDTO;
import com.example.gamemate.domain.friend.dto.FriendResponseDTO;
import com.example.gamemate.domain.user.dto.UserDTO;
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
import java.util.stream.Collectors;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public FriendResponseDTO sendFriendRequest(FriendPostDTO friendPostDto) {
        User requester = userRepository.findById(friendPostDto.getRequesterId())
                .orElseThrow(() -> new InvalidUserIdException("Invalid requester ID"));
        User receiver = userRepository.findById(friendPostDto.getReceiverId())
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        Optional<Friend> existingFriend = friendRepository.findFriendRelationship(requester.getId(), receiver.getId());

        if (existingFriend.isPresent()) {
            Friend friend = existingFriend.get();
            if (friend.getStatus() == Friend.Status.ACCEPTED) {
                return new FriendResponseDTO("이미 친구인 유저입니다.", Friend.Status.ACCEPTED, requester, receiver);
            } else if (friend.getStatus() == Friend.Status.PENDING) {
                return new FriendResponseDTO("친구 요청이 와있는 대상입니다.", Friend.Status.PENDING, requester, receiver);
            }
        }

        Friend friend = new Friend();
        friend.setRequester(requester);
        friend.setReceiver(receiver);
        friend.setStatus(Friend.Status.PENDING);

        friendRepository.save(friend);

        return new FriendResponseDTO("친구 요청이 완료되었습니다.", friend.getStatus(), requester, receiver);
    }

    public FriendResponseDTO respondToFriendRequest(FriendPutDTO friendPutDto) {
        FriendId friendId = new FriendId(friendPutDto.getRequesterId(), friendPutDto.getReceiverId());
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        if (friendPutDto.getStatus() == Friend.Status.ACCEPTED) {
            friend.setStatus(Friend.Status.ACCEPTED);
            friendRepository.save(friend);
            return new FriendResponseDTO("친구 요청을 수락하였습니다.", friend.getStatus(), friend.getRequester(), friend.getReceiver());
        } else if (friendPutDto.getStatus() == Friend.Status.REJECTED) {
            friendRepository.delete(friend);
            return new FriendResponseDTO("친구 요청을 거절하였습니다.", Friend.Status.REJECTED, friend.getRequester(), friend.getReceiver());
        }

        return new FriendResponseDTO("잘못된 요청 상태입니다.", friend.getStatus(), friend.getRequester(), friend.getReceiver());
    }

    public List<UserDTO> getFriends(Long userId) {
        List<User> friends = friendRepository.findFriendUsersByUserId(userId, Friend.Status.ACCEPTED);
        return friends.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getNickname(),
                        user.getRole().name(),
                        user.isDeleted(),
                        user.getPreferredGenres().stream().map(genre -> genre.getName()).collect(Collectors.toList()),
                        user.getPlayTimes().stream().map(playtime -> playtime.getTimeSlot()).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public String deleteFriend(Long requesterId, Long receiverId) {
        FriendId friendId = new FriendId(requesterId, receiverId);
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new InvalidUserIdException("Invalid receiver ID"));

        friendRepository.delete(friend);
        return "친구 삭제가 완료되었습니다.";
    }

    public List<Friend> getPendingFriendRequests(Long receiverId) {
        return friendRepository.findPendingRequestsByReceiverId(receiverId);
    }
}

