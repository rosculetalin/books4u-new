package com.example.springsocial.model;

import com.example.springsocial.model.compositeKey.UserRelationshipKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_relationship")
public class UserRelationship {
    @EmbeddedId
    private UserRelationshipKey id;

    @ManyToOne
    @MapsId("userSenderId")
    @JoinColumn(name = "user_sender_id")
    private User userSender;
    @ManyToOne
    @MapsId("userReceiverId")
    @JoinColumn(name = "user_receiver_id")
    private User userReceiver;

    private RelationshipStatus friendshipStatus;
}

