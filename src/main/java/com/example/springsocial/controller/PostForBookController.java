package com.example.springsocial.controller;

import com.example.springsocial.dtos.PostDto;
import com.example.springsocial.dtos.PostForBookDto;
import com.example.springsocial.model.*;
import com.example.springsocial.model.compositeKey.UserBookKey;
import com.example.springsocial.repository.BookRepository;
import com.example.springsocial.repository.PostForBookRepository;
import com.example.springsocial.repository.UserBookRepository;
import com.example.springsocial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static com.example.springsocial.controller.ResponseMessageHelper.C200;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class PostForBookController {
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;
    private final PostForBookRepository postForBookRepository;

    @PostMapping("/postForBook")
    public ResponseEntity<?> addPost(@RequestBody PostForBookDto postForBookDto){
        long userId = postForBookDto.getUserId();
        long bookId = postForBookDto.getBookId();
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userId), C404);
        }
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, bookId), C404);
        }
        Optional<UserBook> optionalUserBook = userBookRepository.findById(
                UserBookKey.builder().userId(userId).bookId(bookId).build());
        if(!optionalUserBook.isPresent()){
            return getResponseEntity(false, format(USER_BOOK_NOT_FOUND, bookId, userId), C404);
        }
        postForBookRepository.save(PostForBook.builder()
                .userBook(optionalUserBook.get()).text(postForBookDto.getText()).build());
        return getResponseEntity(true, POST_CREATED, C200);
    }

    @GetMapping("/postForBook/{id}")
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
        List<UserBook> userBookList = userBookRepository.findAllByUser(optionalUser.get());
        if(userBookList.isEmpty()){
            return getResponseEntity(true, USER_BOOK_EMPTY, C200);
        }
        List<PostForBook> postList2 = postForBookRepository.findAllByUserBookIn(userBookList);
        return new ResponseEntity<>(postList2, C200);
    }
}
