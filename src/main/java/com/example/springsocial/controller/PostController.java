package com.example.springsocial.controller;

import com.example.springsocial.dtos.PostDto;
import com.example.springsocial.model.Post;
import com.example.springsocial.model.User;
import com.example.springsocial.repository.PostRepository;
import com.example.springsocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto){
        Optional<User> optionalUser = userRepository.findById(postDto.getUserId());
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, postDto.getUserId()), C404);
        }
        postRepository.save(Post.builder().user(optionalUser.get()).text(postDto.getText()).build());
        return getResponseEntity(true, POST_CREATED, C200);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPosts(@PathVariable String id){
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
        List<Post> postList = postRepository.findAllByUser(optionalUser.get());
        return new ResponseEntity<>(postList, C200);
    }
}
