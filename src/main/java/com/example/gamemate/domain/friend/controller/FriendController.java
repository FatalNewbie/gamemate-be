package com.example.gamemate.domain.friend.controller;

import com.example.gamemate.domain.friend.dto.FriendPostDTO;
import com.example.gamemate.domain.friend.dto.FriendPutDTO;
import com.example.gamemate.domain.friend.dto.FriendResponseDTO;
import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.user.dto.UserDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.friend.service.FriendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@Tag(name = "Friend", description = "친구 관리 API")
public class FriendController {

    private final FriendService friendService;


    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/")
    public ResponseEntity<FriendResponseDTO> sendFriendRequest(@RequestBody FriendPostDTO friendPostDto) {
        FriendResponseDTO response = friendService.sendFriendRequest(friendPostDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/")
    public ResponseEntity<FriendResponseDTO> respondToFriendRequest(@RequestBody FriendPutDTO friendPutDto) {
        FriendResponseDTO response = friendService.respondToFriendRequest(friendPutDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable Long userId) {
        List<UserDTO> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests/{receiverId}")
    public ResponseEntity<List<Friend>> getPendingFriendRequests(@PathVariable Long receiverId) {
        List<Friend> pendingRequests = friendService.getPendingFriendRequests(receiverId);
        return ResponseEntity.ok(pendingRequests);
    }
}
