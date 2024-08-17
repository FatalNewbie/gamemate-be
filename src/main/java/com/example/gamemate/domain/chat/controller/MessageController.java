package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.entity.Message;
import com.example.gamemate.domain.chat.model.message.MessageModel;
import com.example.gamemate.domain.chat.model.message.OutputMessageModel;
import com.example.gamemate.domain.chat.service.MessageService;
import com.example.gamemate.domain.user.dto.MyPageResponseDTO;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MessageController {

    //      클라이언트에서 보내오는 메시지모델
//    let chatMessage = {
//              sender: sessionStorage.getItem("username"),
//              content: messageInput.value,
//              chatUuid: chatUuid,
//              type: 'CHAT'
//      };
//

    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/message/send/{roomId}") // "/app/message/send/"+roomId 클라이언트쪽에서 메시지를 보내오는 경로
    @SendTo("/topic/chat/{roomId}") // 특정 목적지를 설정. 메시지브로커는 이 경로에 해당하는 응답채널을 통해서 구독자에게 메시지를 전달할 수 있게 된다.
    public OutputMessageModel sendMessage(@Payload MessageModel messageModel,
                                          @DestinationVariable String roomId
                                          //,@AuthenticationPrincipal UserDetails userDetails //이걸 사용하면 메시지가 이 메소드에 못들어옴.
                                          , SimpMessageHeaderAccessor headerAccessor

    ) { // chatUuid 이 경로로부터 데이터를 추출할 수 있음. 그러기 위해서 @DestinationVariable 사용
        final String time = new SimpleDateFormat("HH:mm").format(new Date());

        Authentication auth = (Authentication) headerAccessor.getUser();
        //  인증 과정에서 username만 사용하여 principal이 String으로 반환됨.
        //UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = (String)auth.getPrincipal();

        MyPageResponseDTO user = userService.findByUsernameForMyPage(username);

        Message newMessage =messageService.saveMessage(messageModel.getChatRoomId(),
                messageModel.getContent(), username, time, messageModel.getType() );

        return new OutputMessageModel(newMessage.getId(),
                user.getNickname(),
                messageModel.getChatRoomId(),
                messageModel.getContent(),
                time,
                messageModel.getType(),newMessage.getWriter().getId(), newMessage.getWriter().getUserProfile());

    }
    // MessageMapping의 경로로 클라이언트가 메시지를 보내오면 sendframe에 담사어 전송함.
//    위 메소드로 그 메시지를 가공해서 리턴하면 응답채널을 통해서 메시지브로커가 받음
    // 그 메시지 브로커는  그 경로를 인지한다음에 클라이언트에게 보내준다.


    @GetMapping("/message/{roomId}/{messageId}")
    public ResponseEntity<List<OutputMessageModel>> getMessagesAfter(@PathVariable Long roomId,@PathVariable Long messageId){
        List<OutputMessageModel> messages = messageService.getMessagesByRoomIdAndMessageId(roomId,messageId);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("message/{id}")
    public ResponseEntity<String> deleteMessageById(@PathVariable Long id){
        Pair<HttpStatus, String> result = messageService.deleteMessageById(id);
        return ResponseEntity.status(result.getFirst()).body(result.getSecond());
    }



}
