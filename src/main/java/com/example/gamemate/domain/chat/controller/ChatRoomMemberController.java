package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateRequest;
import com.example.gamemate.domain.chat.model.chatroom.ChatRoomCreateResponse;
import com.example.gamemate.domain.chat.model.chatroommember.AddMemberRequest;
import com.example.gamemate.domain.chat.model.chatroommember.AddMemberResponse;
import com.example.gamemate.domain.chat.service.ChatRoomMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRoomMemberController {

    private final ChatRoomMemberService chatRoomMemberService;

    @PostMapping("/addmember")
    public AddMemberResponse addMember(@RequestBody AddMemberRequest request){
        boolean isLeader = false;
        return chatRoomMemberService.addMember(request.getChatRoomId(),request.getUser(),isLeader);
    }


}
