package com.example.gamemate.domain.chat.service;

import com.example.gamemate.domain.chat.domain.ChatRoom;
import com.example.gamemate.domain.chat.domain.Message;
import com.example.gamemate.domain.chat.dto.MessageDTO;
import com.example.gamemate.domain.chat.mapper.MessageMapper;
import com.example.gamemate.domain.chat.repository.ChatRoomRepository;
import com.example.gamemate.domain.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    public Message saveMessage(Long chatRoomId, String content, String writer){


        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);


        Message message = new Message(content,chatRoom,writer);

        return messageRepository.save(message);
    }

    public List<MessageDTO> getAllMessagesByRoomId(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        List<Message> messages = messageRepository.findAllByChatRoom(chatRoom);
        return messages.stream()
                .map(MessageMapper::toDTO)
                .collect(Collectors.toList());

    }

}
