package com.example.springsocial.controller;

import com.example.springsocial.dtos.BookDto;
import com.example.springsocial.model.Author;
import com.example.springsocial.model.Book;
import com.example.springsocial.model.User;
import com.example.springsocial.model.UserBook;
import com.example.springsocial.model.compositeKey.UserBookKey;
import com.example.springsocial.repository.AuthorRepository;
import com.example.springsocial.repository.BookRepository;
import com.example.springsocial.repository.UserBookRepository;
import com.example.springsocial.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final UserRepository userRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final UserBookRepository userBookRepository;

    @PostMapping("/book")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookDto bookDto) throws MethodArgumentNotValidException {
        Book book = Book.builder().name(bookDto.getName()).description(bookDto.getDescription()).build();
        String additionalMessage = ".";
        if(bookDto.getAuthorId() != null){
            Optional<Author> optionalAuthor = authorRepository.findById(bookDto.getAuthorId());
            if(optionalAuthor.isPresent()){
                book.setAuthor(optionalAuthor.get());
            } else {
                additionalMessage = format(AUTHOR_NOT_FOUND, bookDto.getAuthorId());
            }
        } else {
            additionalMessage = format(AUTHOR_NOT_FOUND, bookDto.getAuthorId());
        }
        bookRepository.save(book);
        return getResponseEntity(true, BOOK_CREATED + additionalMessage, C201);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBook(@PathVariable String id){
        long bookId;
        try {
            bookId = Long.parseLong(id);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_BOOK_NOT_PARSED, id), C400);
        }
        Optional<?> optionalBook = bookRepository.findById(bookId);
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, id), C404);
        }
        return new ResponseEntity<>(optionalBook.get(), HttpStatus.valueOf(200));
    }

    @PostMapping("/book/{id}")
    public ResponseEntity<?> addBookToUser(@PathVariable String id, @RequestParam String userId) {
        long bookIdentifier, userIdentifier;
        try {
            bookIdentifier = Long.parseLong(id);
            userIdentifier = Long.parseLong(userId);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_USER_BOOK_NOT_PARSED, id, userId), C400);
        }
        Optional<Book> optionalBook = bookRepository.findById(bookIdentifier);
        Optional<User> optionalUser = userRepository.findById(userIdentifier);
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, id), C404);
        }
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userId), C404);
        }
        UserBook userBook = new UserBook();
        userBook.setUser(optionalUser.get());
        userBook.setBook(optionalBook.get());
        UserBookKey userBookKey = new UserBookKey(optionalUser.get().getId(), optionalUser.get().getId());
        userBook.setId(userBookKey);
        try{
            userBookRepository.save(userBook);
        }catch (Exception e){
            return getResponseEntity(false, USER_BOOK_DUPLICATE, C500);
        }
        return getResponseEntity(false, USER_BOOK_CREATED, C201);
    }

}
