package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateRequest;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.service.ChatRoomService;

import com.example.gamemate.global.apiRes.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/")
    public ResponseEntity<ChatRoomDTO> createChatRoom(@RequestBody ChatRoomCreateRequest request,
                                                @AuthenticationPrincipal UserDetails userDetails){
        //return ApiResponse.successRes(HttpStatus.OK,chatRoomService.createChatRoom(request,userDetails));
        return ResponseEntity.ok(chatRoomService.createChatRoom(request,userDetails));
    }

    @GetMapping("/")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(chatRoomService.getAllChatRoomsByUser(userDetails));
        //return ApiResponse.successRes(HttpStatus.OK,chatRoomService.getAllChatRoomsByUser(userDetails));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Long> getChatRoomMemberCnt(@PathVariable Long roomId){
        return ResponseEntity.ok(chatRoomService.getChatRoomMemberCnt(roomId));
        //return ApiResponse.successRes(HttpStatus.OK,chatRoomService.getChatRoomMemberCnt(roomId));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Long> deleteChatRoom(@PathVariable Long roomId){
        chatRoomService.deleteChatRoom(roomId);
        //return ApiResponse.successRes(HttpStatus.OK,roomId);
        return ResponseEntity.ok(roomId);
    }


}
