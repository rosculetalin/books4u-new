package com.example.springsocial.info;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookInfo {
    private Long bookId;
    private String bookName;
    private String bookDescription;
    private Boolean publicVisibility;
    private Boolean openForOffers;
    private Boolean wantToRead;
    private Boolean favorites;
    private Long authorId;
    private String authorName;
    private String authorDescription;
}
