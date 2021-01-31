package com.example.springsocial.controller;

import com.example.springsocial.repository.BookRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookRequestController {

    private final BookRequestRepository bookRequestRepository;


}
