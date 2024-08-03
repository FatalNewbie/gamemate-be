package com.example.gamemate.domain.friend.controller;

import com.example.gamemate.domain.friend.dto.FriendPostDto;
import com.example.gamemate.domain.friend.dto.FriendPutDto;
import com.example.gamemate.domain.friend.dto.FriendResponseDto;
import com.example.gamemate.domain.friend.entity.Friend;
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

    @PostMapping("/request")
    public ResponseEntity<FriendResponseDto> sendFriendRequest(@RequestBody FriendPostDto friendPostDto) {
        FriendResponseDto response = friendService.sendFriendRequest(friendPostDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/respond")
    public ResponseEntity<FriendResponseDto> respondToFriendRequest(@RequestBody FriendPutDto friendPutDto) {
        FriendResponseDto response = friendService.respondToFriendRequest(friendPutDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getFriends(@PathVariable Long userId) {
        List<User> friends = friendService.getFriendsByStatus(userId, Friend.Status.ACCEPTED);
        return ResponseEntity.ok(friends);
    }
}
