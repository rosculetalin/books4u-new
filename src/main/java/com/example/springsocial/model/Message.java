package com.example.springsocial.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

    private String messageValue;

    private String datetime;

    @PrePersist
    protected void prePersist() {
        if (this.datetime == null) {
            Instant instantDate = Instant.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            LocalDateTime localDateTime = instantDate.atZone(ZoneId.systemDefault()).toLocalDateTime();
            this.datetime = localDateTime.format(formatter);

        }
    }
}
