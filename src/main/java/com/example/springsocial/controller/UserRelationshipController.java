package com.example.springsocial.controller;

import com.example.springsocial.dtos.UsersKeyDto;
import com.example.springsocial.dtos.UserRelationshipDto;
import com.example.springsocial.model.User;
import com.example.springsocial.model.UserRelationship;
import com.example.springsocial.model.compositeKey.UsersKey;
import com.example.springsocial.repository.UserRelationshipRepository;
import com.example.springsocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class UserRelationshipController {

    private final UserRelationshipRepository userRelationshipRepository;

    private final UserRepository userRepository;

    @GetMapping("/relationship")
    public ResponseEntity<?> getUserRelationship(@RequestBody UsersKeyDto usersKeyDto){
        long senderId = usersKeyDto.getSenderId();
        long receiverId = usersKeyDto.getReceiverId();
        Optional<User> optionalUserSender = userRepository.findById(senderId);
        Optional<User> optionalUserReceiver = userRepository.findById(receiverId);
        if(!optionalUserSender.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, senderId), C404);
        }
        if(!optionalUserReceiver.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, receiverId), C404);
        }
        UsersKey usersKey = new UsersKey(senderId, receiverId);
        Optional<UserRelationship> optionalUserRelationship = userRelationshipRepository.findById(usersKey);
        if(!optionalUserRelationship.isPresent()){
            return getResponseEntity(false, format(USER_RELATIONSHIP_NOT_FOUND, senderId, receiverId), C404);
        }
        return new ResponseEntity<>(optionalUserRelationship.get(), C200);
    }

    @PostMapping("/relationship/add")
    public ResponseEntity<?> addUserRelationship(@RequestBody UserRelationshipDto userRelationshipDto){
        long senderId = userRelationshipDto.getSenderId();
        long receiverId = userRelationshipDto.getReceiverId();
        Optional<User> optionalUserSender = userRepository.findById(senderId);
        Optional<User> optionalUserReceiver = userRepository.findById(receiverId);
        if(!optionalUserSender.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, senderId), C404);
        }
        if(!optionalUserReceiver.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, receiverId), C404);
        }
        UserRelationship userRelationship = UserRelationship.builder()
                .id(UsersKey.builder().userSenderId(senderId).userReceiverId(receiverId).build())
                .userSender(optionalUserSender.get())
                .userReceiver(optionalUserReceiver.get())
                .friendshipStatus(userRelationshipDto.getRelationshipStatus()).build();
        userRelationshipRepository.save(userRelationship);
        return getResponseEntity(true, USER_RELATIONSHIP_CREATED, C200);
    }

}
