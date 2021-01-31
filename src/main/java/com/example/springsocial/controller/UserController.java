package com.example.springsocial.controller;

import com.example.springsocial.model.User;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Optional<?> optionalUser = userRepository.findById(userPrincipal.getId());
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userPrincipal.getId()), C404);
        }
        return new ResponseEntity<>(optionalUser.get(), C201);
    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id){
        long userId;
        try {
            userId = Long.parseLong(id);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_USER_NOT_PARSED, id), C400);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userId), C404);
        }
        System.out.println(org.hibernate.Version.getVersionString());
        return new ResponseEntity<>(optionalUser.get(), C200);
    }
}
