package com.example.springsocial.controller;

import com.example.springsocial.dtos.AuthorDto;
import com.example.springsocial.model.Author;
import com.example.springsocial.repository.AuthorRepository;

import javax.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static com.example.springsocial.controller.ResponseMessageHelper.*;
import static java.text.MessageFormat.format;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @PostMapping("/author")
    public ResponseEntity<?> addAuthor(@RequestBody @Valid AuthorDto authorDto) throws MethodArgumentNotValidException {
        Author author = Author.builder().name(authorDto.getName()).description(authorDto.getDescription()).build();
        authorRepository.save(author);
        return getResponseEntity(true, AUTHOR_CREATED, C201);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<?> getAuthor(@PathVariable String id){
        long authorId;
        try{
            authorId = Long.parseLong(id);
        } catch(NumberFormatException e){
            return getResponseEntity(false, format(ID_AUTHOR_NOT_PARSED, id), C400);
        }
        Optional<?> optionalAuthor = authorRepository.findById(authorId);
        if(!optionalAuthor.isPresent()){
            return getResponseEntity(false, format(AUTHOR_NOT_FOUND), C404);
        }
        return new ResponseEntity<>(optionalAuthor.get(), HttpStatus.valueOf(200));
    }
}
