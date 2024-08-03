package com.example.gamemate.domain.chat.controller;

import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.chat.dto.MessageDTO;
import com.example.gamemate.domain.chat.model.message.MessageModel;
import com.example.gamemate.domain.chat.model.message.OutputMessageModel;
import com.example.gamemate.domain.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/message/send/{roomId}") // "/app/message/send/"+roomId 클라이언트쪽에서 메시지를 보내오는 경로
    @SendTo("/topic/chat/{roomId}") // 특정 목적지를 설정. 메시지브로커는 이 경로에 해당하는 응답채널을 통해서 구독자에게 메시지를 전달할 수 있게 된다.
    public OutputMessageModel sendMessage(@Payload MessageModel messageModel, @DestinationVariable String roomId) { // chatUuid 이 경로로부터 데이터를 추출할 수 있음. 그러기 위해서 @DestinationVariable 사용
        final String time = new SimpleDateFormat("HH:mm").format(new Date());

        Message newMessage =messageService.saveMessage(messageModel.getChatRoomId(),
                messageModel.getContent(),
                messageModel.getWriter());

        return new OutputMessageModel(newMessage.getId(),
                messageModel.getWriter(),
                messageModel.getChatRoomId(),
                messageModel.getContent(),
                time,
                OutputMessageModel.MessageType.CHAT);

    }
    // MessageMapping의 경로로 클라이언트가 메시지를 보내오면 sendframe에 담사어 전송함.
//    위 메소드로 그 메시지를 가공해서 리턴하면 응답채널을 통해서 메시지브로커가 받음
    // 그 메시지 브로커는  그 경로를 인지한다음에 클라이언트에게 보내준다.


    @GetMapping("/message/{roomId}")
    public ResponseEntity<List<MessageDTO>> getAllMessagesByRoomId(@PathVariable Long roomId){
        List<MessageDTO> messages = messageService.getAllMessagesByRoomId(roomId);
        return ResponseEntity.ok(messages);
    }

}
