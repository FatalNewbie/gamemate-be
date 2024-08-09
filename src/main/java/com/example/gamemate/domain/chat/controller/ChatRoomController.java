package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateRequest;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.service.ChatRoomService;

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
    public ChatRoomCreateResponse createChatRoom(@RequestBody ChatRoomCreateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails){
        return chatRoomService.createChatRoom(request,userDetails);
    }

    @GetMapping("/")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms(@AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok(chatRoomService.getAllChatRoomsByUser(userDetails));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Long> getChatRoomMemberCnt(@PathVariable Long roomId){

        Long result = chatRoomService.getChatRoomMemberCnt(roomId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long roomId){
        Pair<HttpStatus, String> result = chatRoomService.deleteChatRoom(roomId);
        return ResponseEntity.status(result.getFirst()).body(result.getSecond());
    }
    }
