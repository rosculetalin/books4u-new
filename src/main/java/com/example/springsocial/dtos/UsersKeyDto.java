package com.example.springsocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UsersKeyDto {
    @NotNull
    Long senderId;
    @NotNull
    Long receiverId;
}
