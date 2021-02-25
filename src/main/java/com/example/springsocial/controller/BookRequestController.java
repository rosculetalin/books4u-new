package com.example.springsocial.controller;

import com.example.springsocial.dtos.BookRequestDto;
import com.example.springsocial.model.Book;
import com.example.springsocial.model.BookRequest;
import com.example.springsocial.model.User;
import com.example.springsocial.model.UserBook;
import com.example.springsocial.model.compositeKey.UserBookKey;
import com.example.springsocial.repository.BookRepository;
import com.example.springsocial.repository.BookRequestRepository;
import com.example.springsocial.repository.UserBookRepository;
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
public class BookRequestController {

    private final BookRequestRepository bookRequestRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final UserBookRepository userBookRepository;

    @PostMapping("/book/request")
    public ResponseEntity<?> addBookRequest(@RequestBody BookRequestDto bookRequestDto){
        long senderId = bookRequestDto.getUserSender();
        long receiverId = bookRequestDto.getUser();
        long bookId = bookRequestDto.getBook();
        Optional<User> optionalUserSender = userRepository.findById(senderId);
        Optional<User> optionalUserReceiver = userRepository.findById(receiverId);
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(!optionalUserSender.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, senderId), C404);
        }
        if(!optionalUserReceiver.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, receiverId), C404);
        }
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, bookId), C404);
        }
        Optional<UserBook> optionalUserBook = userBookRepository.findById(UserBookKey.builder()
            .bookId(bookId).userId(receiverId).build());
        if(!optionalUserBook.isPresent()){
            return getResponseEntity(false, format(USER_BOOK_NOT_FOUND, bookId, receiverId), C404);
        }
        BookRequest bookRequest = BookRequest.builder()
                .userSender(optionalUserSender.get())
                .userBook(optionalUserBook.get())
                .bookRequestStatus(bookRequestDto.getBookRequestStatus())
                .build();
        bookRequestRepository.save(bookRequest);
        return getResponseEntity(true, BOOK_REQUEST_CREATED, C201);
    }

    @GetMapping("/book/request/send/{id}")
    public ResponseEntity<?> getBookRequestSend(@PathVariable String id){
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
        List<BookRequest> bookRequestList = bookRequestRepository.findAllByUserSender(optionalUser.get());
        return new ResponseEntity<>(bookRequestList, C200);
    }
}
