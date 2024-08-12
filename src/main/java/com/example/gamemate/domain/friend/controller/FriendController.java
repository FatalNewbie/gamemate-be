package com.example.gamemate.domain.friend.controller;

import com.example.gamemate.domain.friend.dto.FriendPostDTO;
import com.example.gamemate.domain.friend.dto.FriendPutDTO;
import com.example.gamemate.domain.friend.dto.FriendResponseDTO;
import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.friend.repository.FriendRepository;
import com.example.gamemate.domain.user.dto.UserDTO;
import com.example.gamemate.domain.friend.service.FriendService;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.apiRes.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/friend")
@Tag(name = "Friend", description = "친구 관리 API")
public class FriendController {

    private final FriendService friendService;
    private final UserRepository userRepository;

    public FriendController(FriendService friendService, UserRepository userRepository) {
        this.friendService = friendService;
        this.userRepository = userRepository;
    }

    @PostMapping("/")
    public ApiResponse<FriendResponseDTO> sendFriendRequest(
            @RequestBody FriendPostDTO friendPostDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User requester = userRepository.findByUsername(userDetails.getUsername());
        friendPostDto.setRequesterId(requester.getId());
        FriendResponseDTO response = friendService.sendFriendRequest(userDetails.getUsername(), friendPostDto);
        return ApiResponse.successRes(HttpStatus.OK, response);
    }

    @PutMapping("/respond")
    public ApiResponse<FriendResponseDTO> respondToFriendRequest(
            @RequestBody FriendPutDTO friendPutDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User receiver = userRepository.findByUsername(userDetails.getUsername());
        friendPutDto.setReceiverId(receiver.getId());
        FriendResponseDTO response = friendService.respondToFriendRequest(userDetails.getUsername(), friendPutDto);
        return ApiResponse.successRes(HttpStatus.OK, response);
    }

    @PutMapping("/cancel")
    public ApiResponse<FriendResponseDTO> cancelFriendRequest(
            @RequestBody FriendPutDTO friendPutDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User requester = userRepository.findByUsername(userDetails.getUsername());
        friendPutDto.setRequesterId(requester.getId());
        FriendResponseDTO response = friendService.cancelFriendRequest(userDetails.getUsername(), friendPutDto);
        return ApiResponse.successRes(HttpStatus.OK, response);
    }

    @DeleteMapping("/{friendId}")
    public ApiResponse<FriendResponseDTO> deleteFriend(
            @PathVariable Long friendId,
            @AuthenticationPrincipal UserDetails userDetails) {

        FriendResponseDTO response = friendService.deleteFriend(userDetails.getUsername(), friendId);
        return ApiResponse.successRes(HttpStatus.OK, response);
    }


    @GetMapping("/")
    public ApiResponse<List<UserDTO>> getFriends(@AuthenticationPrincipal UserDetails userDetails) {
        List<UserDTO> friends = friendService.getFriends(userDetails.getUsername());
        return ApiResponse.successRes(HttpStatus.OK, friends);
    }

    @GetMapping("/received-requests")
    public ApiResponse<List<FriendResponseDTO>> getReceivedFriendRequests(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<FriendResponseDTO> pendingRequests = friendService.getReceivedFriendRequests(username);
        return ApiResponse.successRes(HttpStatus.OK, pendingRequests);
    }

    @GetMapping("/sent-requests")
    public ApiResponse<List<FriendResponseDTO>> getSentFriendRequests(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        List<FriendResponseDTO> pendingRequests = friendService.getSentFriendRequests(username);
        return ApiResponse.successRes(HttpStatus.OK, pendingRequests);
    }
}

