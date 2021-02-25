package com.example.springsocial.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostForBookDto {
    private Long userId;
    private Long bookId;
    private String text;
}
