package com.example.springsocial.model;

import com.example.springsocial.model.compositeKey.UserBookKey;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_book")
public class UserBook {
    @EmbeddedId
    private UserBookKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean publicVisibility;
    private boolean openForOffers;
}
