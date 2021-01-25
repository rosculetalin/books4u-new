package com.example.springsocial.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="user_sender", referencedColumnName="user_sender_id"),
        @JoinColumn(name="user_receiver", referencedColumnName="user_receiver_id")
    })
    private Chat chat;

    private Boolean isSender;

    private String datetime;
}
