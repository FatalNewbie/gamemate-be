package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
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



    @GetMapping("/")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(chatRoomService.getAllChatRoomsByUser(userDetails));
        //return ApiResponse.successRes(HttpStatus.OK,chatRoomService.getAllChatRoomsByUser(userDetails));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Long> getAllChatRooms(@PathVariable Long postId,
                                                @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(chatRoomService.getChatRoomByPostId(postId));
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
