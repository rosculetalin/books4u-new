package com.example.springsocial.model;

import com.example.springsocial.model.compositeKey.ChatKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "chat")
public class Chat {
    @EmbeddedId
    private ChatKey id;

    @ManyToOne
    @MapsId("userSenderId")
    @JoinColumn(name = "user_sender_id")
    private User userSender;
    @ManyToOne
    @MapsId("userReceiverId")
    @JoinColumn(name = "user_receiver_id")
    private User userReceiver;
}
