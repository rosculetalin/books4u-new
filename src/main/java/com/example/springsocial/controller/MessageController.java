package com.example.springsocial.controller;

import com.example.springsocial.dtos.MessageConversationDto;
import com.example.springsocial.dtos.MessageDto;
import com.example.springsocial.model.Chat;
import com.example.springsocial.model.Message;
import com.example.springsocial.model.User;
import com.example.springsocial.model.compositeKey.ChatKey;
import com.example.springsocial.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    private final MessageRepository messageRepository;

    @PostMapping("/message")
    public ResponseEntity<?> addMessage(@RequestBody @Valid MessageDto messageDto) throws MethodArgumentNotValidException {
        ChatKey chatKey = new ChatKey(messageDto.getChatId().getUserSenderId(), messageDto.getChatId().getUserReceiverId());
        Optional<Chat> optionalChat = chatRepository.findById(chatKey);
        if(!optionalChat.isPresent()){
            return getResponseEntity(false, String.format(CHAT_NOT_FOUND, chatKey.getUserReceiverId(), chatKey.getUserSenderId()), C404);
        }
        Message message = new Message();
        message.setChat(optionalChat.get());
        message.setIsSender(messageDto.getIsSender());
        message.setMessageValue(messageDto.getMessageValue());
        messageRepository.save(message);
        return getResponseEntity(true, MESSAGE_CREATED, C201);
    }

    @GetMapping("/message/all")
    public ResponseEntity<?> getMessages(@RequestParam String userSender, @RequestParam String userReceiver){
        long userSenderId, userReceiverId;
        try{
            userSenderId = Long.parseLong(userSender);
            userReceiverId = Long.parseLong(userReceiver);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_USERS_NOT_PARSED, userSender, userReceiver), C400);
        }
        Optional<User> optionalUserSender = userRepository.findById(userSenderId);
        Optional<User> optionalUserReceiver = userRepository.findById(userReceiverId);
        if(!optionalUserSender.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userSender), C404);
        }
        if(!optionalUserReceiver.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userReceiver), C404);
        }
        ChatKey chatKeySenderCase = new ChatKey(optionalUserSender.get().getId(), optionalUserReceiver.get().getId());
        Optional<Chat> optionalChatSenderCase = chatRepository.findById(chatKeySenderCase);
        List<MessageConversationDto> messages;
        if(!optionalChatSenderCase.isPresent()){
            ChatKey chatKeyReceiverCase = new ChatKey();
            chatKeyReceiverCase.setUserSenderId(optionalUserReceiver.get().getId());
            chatKeyReceiverCase.setUserReceiverId(optionalUserSender.get().getId());
            Optional<Chat> optionalChatReceiverCase = chatRepository.findById(chatKeyReceiverCase);
            if(!optionalChatReceiverCase.isPresent()){
                return getResponseEntity(false, format(CHAT_NOT_FOUND, userSender, userReceiver), C404);
            } else {
                messages = messageRepository.findAllByOrderByDatetimeAsc().stream().map(message -> {
                    return MessageConversationDto.builder()
                            .datetime(message.getDatetime())
                            .isCurrent(!message.getIsSender())
                            .messageValue(message.getMessageValue()).build();
                }).collect(Collectors.toList());
            }
        } else {
            messages = messageRepository.findAllByOrderByDatetimeAsc().stream().map(message -> {
                return MessageConversationDto.builder()
                        .datetime(message.getDatetime())
                        .isCurrent(message.getIsSender())
                        .messageValue(message.getMessageValue()).build();
            }).collect(Collectors.toList());
        }
        return new ResponseEntity<>(messages, C200);
    }
}
