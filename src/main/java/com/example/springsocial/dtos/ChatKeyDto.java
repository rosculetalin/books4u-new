package com.example.springsocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatKeyDto {
    @NotNull
    private Long userSenderId;
    @NotNull
    private Long userReceiverId;
}
