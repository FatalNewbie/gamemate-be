package com.example.gamemate.domain.chat.service;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.chat.dto.MessageDTO;
import com.example.gamemate.domain.chat.mapper.MessageMapper;
import com.example.gamemate.domain.chat.model.message.OutputMessageModel;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.chat.repository.MessageRepository;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Message saveMessage(Long chatRoomId, String content, String username, String time ,String type){


        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        User writer =  userRepository.findByUsername(username);

        Message message = new Message(content,chatRoom,writer,time,type);

        return messageRepository.save(message);
    }

    public List<OutputMessageModel> getAllMessagesByRoomId(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        List<Message> messages = messageRepository.findAllByChatRoom(chatRoom);
        return messages.stream()
                .map(MessageMapper::toOutputMessageModel)
                .collect(Collectors.toList());

    }

    public Pair<HttpStatus,String> deleteMessageById(Long id){
        if(messageRepository.existsById(id)){
            messageRepository.deleteById(id);
            return Pair.of(HttpStatus.OK, id + " message deleted");
        } else {
            return Pair.of(HttpStatus.NOT_FOUND, id + " message not found");
        }
    }



}
