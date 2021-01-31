package com.example.springsocial.controller;

import com.example.springsocial.model.Chat;
import com.example.springsocial.model.User;
import com.example.springsocial.model.compositeKey.UsersKey;
import com.example.springsocial.repository.*;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final UserRepository userRepository;

    private final ChatRepository chatRepository;

    @PostMapping("/chat/add")
    public ResponseEntity<?> addChat(@RequestParam String userSender, @RequestParam String userReceiver){
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
        Chat chat = new Chat();
        chat.setUserSender(optionalUserSender.get());
        chat.setUserReceiver(optionalUserReceiver.get());
        UsersKey usersKey = new UsersKey(optionalUserSender.get().getId(), optionalUserReceiver.get().getId());
        chat.setId(usersKey);
        chatRepository.save(chat);
        return getResponseEntity(true, CHAT_CREATED, C201);
    }

    @GetMapping("/chat")
    public ResponseEntity<?> getChat(@RequestParam String userSender, @RequestParam String userReceiver){
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
        UsersKey usersKey = new UsersKey(optionalUserSender.get().getId(), optionalUserReceiver.get().getId());
        Optional<Chat> optionalChat = chatRepository.findById(usersKey);
        if(!optionalChat.isPresent()){
            return getResponseEntity(false, format(CHAT_NOT_FOUND, userSender, userReceiver), C404);
        }
        return new ResponseEntity<>(optionalChat.get(), C200);
    }
}
