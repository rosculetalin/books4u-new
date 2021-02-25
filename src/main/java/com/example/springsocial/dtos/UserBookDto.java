package com.example.springsocial.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBookDto {
    @NotNull
    private Long userId;
    @NotNull
    private Long bookId;
    @NotNull
    private Boolean publicVisibility;
    @NotNull
    private Boolean openForOffers;
    @NotNull
    private Boolean favorites;
    @NotNull
    private Boolean wantToRead;
}
