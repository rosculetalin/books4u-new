package com.example.springsocial.controller;

import com.example.springsocial.dtos.AuthorDto;
import com.example.springsocial.dtos.BookDto;
import com.example.springsocial.exception.unchecked.ResourceNotFoundException;
import com.example.springsocial.model.*;
import com.example.springsocial.model.compositeKey.UserBookKey;
import com.example.springsocial.payload.ApiResponse;
import com.example.springsocial.repository.AuthorRepository;
import com.example.springsocial.repository.BookRepository;
import com.example.springsocial.repository.UserBookRepository;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final UserBookRepository userBookRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        Optional<?> optionalUser = userRepository.findById(userPrincipal.getId());
        if(!optionalUser.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false, "Current user with id " +
                    userPrincipal.getId() + "could not be found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.FOUND);
    }

    @PostMapping("/author")
    public ResponseEntity<?> addAuthor(@RequestBody @Valid AuthorDto authorDto) throws MethodArgumentNotValidException {
        Author author = Author.builder().name(authorDto.getName()).description(authorDto.getDescription()).build();
        authorRepository.save(author);
        return new ResponseEntity<>(new ApiResponse(true, "Author has been created"), HttpStatus.CREATED);
    }

    @GetMapping("/author/{id}")
    public void getAuthor(@PathVariable String id){

    }

    @PostMapping("/book")
    public void addBook(@RequestBody @Valid BookDto bookDto){
        System.out.println("altcevaaa");
        Book book = Book.builder().name(bookDto.getName()).description(bookDto.getDescription()).build();
        Optional<Author> optionalAuthor = authorRepository.findById(bookDto.getAuthorId());
        optionalAuthor.ifPresent(book::setAuthor);
        bookRepository.save(book);
    }

    @PostMapping("/book/{id}")
    public void addBookToUser(@PathVariable String id, @RequestParam String userId) {
        long bookIdentifier = 1, userIdentifier = 1;

            bookIdentifier = Long.parseLong(id);

            userIdentifier = Long.parseLong(userId);

        Optional<Book> optionalBook = bookRepository.findById(bookIdentifier);
        Optional<User> optionalUser = userRepository.findById(userIdentifier);
        UserBook userBook = new UserBook();
        userBook.setUser(optionalUser.get());
        userBook.setBook(optionalBook.get());
        UserBookKey userBookKey = new UserBookKey(optionalUser.get().getId(), optionalUser.get().getId());
        userBook.setId(userBookKey);
        userBookRepository.save(userBook);
        Optional<UserBook> userBookOptional = userBookRepository.findById(userBookKey);
        System.out.println(userBookOptional.get());
    }
}
