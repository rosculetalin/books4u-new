package com.example.springsocial.controller;

import com.example.springsocial.dtos.BookDto;
import com.example.springsocial.dtos.UserBookDto;
import com.example.springsocial.info.BookInfo;
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

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;
import java.util.List;
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

    private final EntityManager entityManager;

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
        book.setImagePresent(true);
        bookRepository.save(book);
        return getResponseEntity(true, BOOK_CREATED + additionalMessage, C201);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id){
        long bookId;
        try {
            bookId = Long.parseLong(id);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_BOOK_NOT_PARSED, id), C400);
        }
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, id), C404);
        }
        bookRepository.delete(optionalBook.get());
        return getResponseEntity(true, BOOK_DELETED, C200);
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

    @GetMapping("/book/all/byUser/{id}")
    public ResponseEntity<?> getAllBooksOfUser(@PathVariable String id){
        long userId;
        try {
            userId = Long.parseLong(id);
        }catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_USER_NOT_PARSED, id), C400);
        }
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, id), C404);
        }
        Query query = entityManager.createQuery("SELECT new com.example.springsocial.info.BookInfo(" +
                "b.id , b.name, b.description, " +
                "ub.publicVisibility, ub.openForOffers, ub.wantToRead, ub.favorites, " +
                "a.id, a.name, a.description) FROM Book b " +
                "JOIN UserBook ub ON b.id = ub.book " +
                "JOIN Author a ON b.author = a.id");
        List<BookInfo> bookInfoList = query.getResultList();
        return new ResponseEntity<>(bookInfoList, C200);
    }

    @GetMapping("/book/all/")
    public ResponseEntity<?> getAllBooksOfUser(){
        List<Book> bookList = bookRepository.findAll();
        return new ResponseEntity<>(bookList, C200);
    }

    @PostMapping("/book/user/")
    public ResponseEntity<?> addBookToUser(@RequestBody @Valid UserBookDto userBookDto) {
        Optional<Book> optionalBook = bookRepository.findById(userBookDto.getBookId());
        Optional<User> optionalUser = userRepository.findById(userBookDto.getUserId());
        if(!optionalBook.isPresent()){
            return getResponseEntity(false, format(BOOK_NOT_FOUND, userBookDto.getBookId()), C404);
        }
        if(!optionalUser.isPresent()){
            return getResponseEntity(false, format(USER_NOT_FOUND, userBookDto.getUserId()), C404);
        }
        UserBook userBook = new UserBook();
        userBook.setUser(optionalUser.get());
        userBook.setBook(optionalBook.get());
        UserBookKey userBookKey = new UserBookKey(optionalUser.get().getId(), optionalBook.get().getId());
        userBook.setId(userBookKey);
        userBook.setPublicVisibility(userBookDto.getPublicVisibility());
        userBook.setOpenForOffers(userBookDto.getOpenForOffers());
        userBook.setFavorites(userBookDto.getFavorites());
        userBook.setWantToRead(userBookDto.getWantToRead());
        try{
            userBookRepository.save(userBook);
        }catch (Exception e){
            return getResponseEntity(false, USER_BOOK_DUPLICATE, C500);
        }
        return getResponseEntity(false, USER_BOOK_CREATED, C201);
    }

}
