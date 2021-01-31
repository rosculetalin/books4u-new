package com.example.springsocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MessageDto {
    @NotNull
    private UsersKeyDto chatId;
    @NotNull
    private Boolean isSender;
    @NotNull
    private String messageValue;
}
