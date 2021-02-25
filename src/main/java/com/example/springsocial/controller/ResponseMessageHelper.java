package com.example.springsocial.controller;

import com.example.springsocial.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ResponseMessageHelper {

    static HttpStatus C200 = HttpStatus.valueOf(200);
    static HttpStatus C201 = HttpStatus.valueOf(201);
    static HttpStatus C400 = HttpStatus.valueOf(400);
    static HttpStatus C404 = HttpStatus.valueOf(404);
    static HttpStatus C500 = HttpStatus.valueOf(500);

    static String ID_USERS_NOT_PARSED = "Id for user {0} or/and {1} could not be parsed to long";
    static String ID_USER_NOT_PARSED = "Id for user {0} could not be parsed to long";
    static String USER_NOT_FOUND = "User sender with id {0} could not be found.";

    static String CHAT_NOT_FOUND = "The chat between user {0} and user {1} could not be found.";
    static String CHAT_CREATED = "The chat has been created.";

    static String ID_AUTHOR_NOT_PARSED = "Id for author {0} could not be parsed to long value.";
    static String AUTHOR_NOT_FOUND = "Author with id {0} could not be found.";
    static String AUTHOR_CREATED = "The author has been created.";

    static String ID_BOOK_NOT_PARSED = "Id for book {0} could not be parsed to long value.";
    static String BOOK_NOT_FOUND = "Book with id {0} could not be found.";
    static String BOOK_CREATED = "The book has been created.";
    static String BOOK_DELETED = "The book has been deleted.";

    static String ID_USER_BOOK_NOT_PARSED = "Id for book {0} or id for user {1} could not be parsed to long.";
    static String USER_BOOK_DUPLICATE = "The book may be already in database";
    static String USER_BOOK_CREATED = "The book has been added to the user.";
    static String USER_BOOK_NOT_FOUND = "The book {0} of user {1} could not be found.";
    static String USER_BOOK_EMPTY = "The user does not have any books.";

    static String BOOK_REQUEST_CREATED = "The book request has been created.";

    static String MESSAGE_CREATED = "The message has been created.";

    static String USER_RELATIONSHIP_NOT_FOUND = "It is no relationship between {0} and {1}.";
    static String USER_RELATIONSHIP_CREATED = "The relationship has been created.";

    static String POST_CREATED = "The post has been created.";

    static String IMAGE_NOT_FOUND = "The user {0} has not a profile image.";
    static String IMAGE_FORMAT_INVALID = "The format of image must be jpeg.";

    static ResponseEntity<?> getResponseEntity(boolean success, String message, HttpStatus status){
        return new ResponseEntity<>(new ApiResponse(success, message), status);
    }

}
