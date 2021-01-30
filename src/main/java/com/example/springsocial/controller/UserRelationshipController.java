package com.example.springsocial.controller;

import com.example.springsocial.repository.UserRelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRelationshipController {

    private final UserRelationshipRepository userRelationshipRepository;
}
