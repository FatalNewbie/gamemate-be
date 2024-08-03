package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.dto.ChatRoomDTO;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateRequest;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/")
    public ChatRoomCreateResponse createChatRoom(@RequestBody ChatRoomCreateRequest request){
        return chatRoomService.createChatRoom(request.getChatTitle(),request.getUser());
    }

    @GetMapping("/")
    public ResponseEntity<List<ChatRoomDTO>> getAllChatRooms(){
        List<ChatRoomDTO> chatRooms = chatRoomService.getAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable Long roomId){
        boolean isDeleted = chatRoomService.deleteChatRoom(roomId);
        if(isDeleted){
            return ResponseEntity.status(HttpStatus.OK).body(roomId+" chatroom deleted");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(roomId+" not found");
        }
    }
    }
