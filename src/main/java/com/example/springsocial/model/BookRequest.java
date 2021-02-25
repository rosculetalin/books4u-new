package com.example.springsocial.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book_request")
public class BookRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn( name = "user_sender_id")
    private User userSender;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "user", referencedColumnName = "user_id"),
        @JoinColumn(name = "book", referencedColumnName = "book_id")
    })
    private UserBook userBook;

    private BookRequestStatus bookRequestStatus;
}
