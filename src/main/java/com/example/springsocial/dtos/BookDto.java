package com.example.springsocial.dtos;

import com.example.springsocial.model.Author;
import com.example.springsocial.model.UserBook;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class BookDto {
    @NotNull @NotEmpty
    private String name;
    @NotNull @NotEmpty
    private String description;
    @NotEmpty
    private Long authorId;
}
