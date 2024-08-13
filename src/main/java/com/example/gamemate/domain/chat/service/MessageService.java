package com.example.gamemate.domain.chat.service;

import com.example.gamemate.domain.chat.entity.ChatRoom;
import com.example.gamemate.domain.chat.entity.Message;
import com.example.gamemate.domain.chat.mapper.MessageMapper;
import com.example.gamemate.domain.chat.model.message.OutputMessageModel;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.chat.repository.MessageRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Value("${message.page.size}")
    private int pageSize;

    public Message saveMessage(Long chatRoomId, String content, String username, String time ,String type){


        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        User writer =  userRepository.findByUsername(username);

        Message message = new Message(content,chatRoom,writer,time,type);

        return messageRepository.save(message);
    }

    public List<OutputMessageModel> getMessagesByRoomIdAndMessageId(Long chatRoomId,Long lastMessageId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);

        List<Message> messages;

        // lastMessageId가 -1이면 최신 메시지를 가져옴
        if (lastMessageId == -1) {
            Pageable pageable = PageRequest.of(0, pageSize); // 환경변수에서 페이지 사이즈 가져오기
            messages = messageRepository.findMessagesByChatRoomIdAndBeforeMessageId(chatRoomId, Long.MAX_VALUE, pageable);
        } else {
            Pageable pageable = PageRequest.of(0, pageSize);
            messages = messageRepository.findMessagesByChatRoomIdAndBeforeMessageId(chatRoomId, lastMessageId, pageable);
        }

        // OutputMessageModel로 변환
        return messages.stream()
                .map(MessageMapper::toOutputMessageModel)
                .collect(Collectors.toList());

    }


//    public List<OutputMessageModel> getMessagesByRoomIdAndMessageId(Long chatRoomId,Long lastMessageId){
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
//        List<Message> messages = messageRepository.findAllByChatRoom(chatRoom);
//        return messages.stream()
//                .map(MessageMapper::toOutputMessageModel)
//                .collect(Collectors.toList());
//
//    }

    public Pair<HttpStatus,String> deleteMessageById(Long id){
        if(messageRepository.existsById(id)){
            messageRepository.deleteById(id);
            return Pair.of(HttpStatus.OK, id + " message deleted");
        } else {
            return Pair.of(HttpStatus.NOT_FOUND, id + " message not found");
        }
    }



}
