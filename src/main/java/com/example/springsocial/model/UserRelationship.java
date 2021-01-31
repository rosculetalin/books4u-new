package com.example.springsocial.model;

import com.example.springsocial.model.compositeKey.UsersKey;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_relationship")
public class UserRelationship {
    @EmbeddedId
    private UsersKey id;

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

