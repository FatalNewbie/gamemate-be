package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateRequest;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.model.chatroommember.AddMemberRequest;
import com.example.gamemate.domain.chat.model.chatroommember.AddMemberResponse;
import com.example.gamemate.domain.chat.service.ChatRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/addmember")
    public AddMemberResponse addMember(@RequestBody AddMemberRequest request){

        return chatRoomMemberService.addMember(request.getChatRoomId(),request.getWriterId(),false);
    }

    @DeleteMapping("/deletemember/{roomId}")
    public ResponseEntity<Long> deleteMember(@PathVariable Long roomId,
                                             @AuthenticationPrincipal UserDetails userDetails){
        chatRoomMemberService.deleteMember(roomId,userDetails);
        //return ApiResponse.successRes(HttpStatus.OK,roomId);
        return ResponseEntity.ok(roomId);
    }


}
