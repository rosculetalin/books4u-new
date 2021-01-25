package com.example.springsocial.dtos;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthorDto {
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String description;
}
