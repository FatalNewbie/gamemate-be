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
import com.example.gamemate.global.exception.FriendExceptionCode;
import com.example.gamemate.global.exception.InvalidUserIdException;
import com.example.gamemate.global.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FriendResponseDTO sendFriendRequest(String requesterName, FriendPostDTO friendPostDto) {
        log.info("수신자 ID{}", friendPostDto.getReceiverId());
        User receiver = userRepository.findById(friendPostDto.getReceiverId())
                .orElseThrow(() -> new RestApiException(FriendExceptionCode.INVALID_USER_ID));

        User requester = userRepository.findByUsername(requesterName);

        Optional<Friend> existingFriend = friendRepository.findFriendRelationship(requester.getId(), receiver.getId());

        if (existingFriend.isPresent()) {
            Friend friend = existingFriend.get();
            if (friend.getStatus() == Friend.Status.ACCEPTED) {
                return new FriendResponseDTO("이미 친구인 유저입니다.",
                        Friend.Status.ACCEPTED,
                        new UserDTO(requester),
                        new UserDTO(receiver),
                        null);
            }

            if (friend.getStatus() == Friend.Status.PENDING) {
                return new FriendResponseDTO("친구 요청이 진행 중인 유저입니다.",
                        Friend.Status.PENDING,
                        new UserDTO(requester),
                        new UserDTO(receiver),
                        null);
            }
        }

        Friend friend = new Friend();
        friend.setRequester(requester);
        friend.setReceiver(receiver);
        friend.setStatus(Friend.Status.PENDING);

        friendRepository.save(friend);

        return new FriendResponseDTO("친구 요청이 완료되었습니다.",
                Friend.Status.PENDING,
                new UserDTO(requester),
                new UserDTO(receiver),
                null);
    }

    @Transactional
    public FriendResponseDTO respondToFriendRequest(String username, FriendPutDTO friendPutDto) {
        User receiver = userRepository.findByUsername(username);

        FriendId friendId = new FriendId(friendPutDto.getRequesterId(), receiver.getId());
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RestApiException(FriendExceptionCode.INVALID_FRIEND_RELATIONSHIP));

        if (friendPutDto.getStatus() == Friend.Status.ACCEPTED) {
            friend.setStatus(Friend.Status.ACCEPTED);
            friend.setAcceptedDate(LocalDateTime.now());
            friendRepository.save(friend);
            return new FriendResponseDTO("친구 요청을 수락하였습니다.",
                    Friend.Status.ACCEPTED,
                    new UserDTO(friend.getRequester()),
                    new UserDTO(friend.getReceiver()),
                    friend.getAcceptedDate());
        }

        if (friendPutDto.getStatus() == Friend.Status.REJECTED) {
            friendRepository.delete(friend);
            return new FriendResponseDTO("친구 요청을 거절하였습니다.",
                    Friend.Status.REJECTED,
                    new UserDTO(friend.getRequester()),
                    new UserDTO(friend.getReceiver()),
                    null);
        }

        return new FriendResponseDTO("잘못된 요청 상태입니다.",
                friend.getStatus(),
                new UserDTO(friend.getRequester()),
                new UserDTO(friend.getReceiver()),
                null);
    }

    @Transactional
    public FriendResponseDTO cancelFriendRequest(String username, FriendPutDTO friendPutDTO) {
        User requester = userRepository.findByUsername(username);

        User receiver = userRepository.findById(friendPutDTO.getReceiverId())
                .orElseThrow(() -> new RestApiException(FriendExceptionCode.INVALID_USER_ID));

        FriendId friendId = new FriendId(requester.getId(), receiver.getId());
        log.info("친구 관계 ID {}", friendId);
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new RestApiException(FriendExceptionCode.INVALID_FRIEND_RELATIONSHIP));

        if (friend.getStatus() == Friend.Status.PENDING) {
            FriendResponseDTO response = new FriendResponseDTO("친구 요청이 취소되었습니다.",
                    Friend.Status.CANCELED,
                    new UserDTO(friend.getRequester()),
                    new UserDTO(friend.getReceiver()),
                    null);
            friendRepository.delete(friend);
            return response;
        } else {
            throw new RestApiException(FriendExceptionCode.INVALID_FRIEND_STATUS);
        }
    }

    @Transactional
    public FriendResponseDTO deleteFriend(String username, Long friendId) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(FriendExceptionCode.INVALID_USER_ID);
        }

        // user가 requester이거나 receiver인 친구 관계를 찾습니다.
        Optional<Friend> optionalFriend = friendRepository.findFriendRelationship(user.getId(), friendId);

        if (optionalFriend.isEmpty() || optionalFriend.get().getStatus() != Friend.Status.ACCEPTED) {
            throw new RestApiException(FriendExceptionCode.INVALID_FRIEND_RELATIONSHIP);
        }

        Friend friend = optionalFriend.get();
        friendRepository.delete(friend);

        return new FriendResponseDTO("친구 관계가 삭제되었습니다.",
                Friend.Status.REJECTED,
                new UserDTO(friend.getRequester()),
                new UserDTO(friend.getReceiver()),
                null);
    }

    public List<UserDTO> getFriends(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(FriendExceptionCode.INVALID_USER_ID);
        }

        List<User> friends = friendRepository.findFriendUsersByUserId(user.getId(), Friend.Status.ACCEPTED);
        return friends.stream()
                .map(friend -> new UserDTO(
                        friend.getId(),
                        friend.getUsername(),
                        friend.getNickname(),
                        friend.getRole().name(),
                        friend.isDeleted(),
                        friend.getPreferredGenres().stream().map(genre -> genre.getName()).collect(Collectors.toList()),
                        friend.getPlayTimes().stream().map(playtime -> playtime.getTimeSlot()).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    public List<FriendResponseDTO> getPendingFriendRequests(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(FriendExceptionCode.INVALID_USER_ID);
        }

        List<Friend> pendingRequests = friendRepository.findPendingRequestsByReceiverId(user.getId());

        return pendingRequests.stream()
                .map(friend -> new FriendResponseDTO(
                        null,
                        friend.getStatus(),
                        new UserDTO(friend.getRequester()),
                        new UserDTO(friend.getReceiver()),
                        null))
                .collect(Collectors.toList());
    }
}

