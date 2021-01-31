package com.example.springsocial.dtos;

import com.example.springsocial.model.BookRequestStatus;
import lombok.Data;

@Data
public class BookRequestDto {
    private Long userSender;
    private Long user;
    private Long book;
    BookRequestStatus bookRequestStatus;
}
