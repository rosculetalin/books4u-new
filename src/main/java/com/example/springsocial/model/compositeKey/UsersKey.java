package com.example.springsocial.model.compositeKey;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersKey implements Serializable {
    @Column(name = "user_sender_id")
    private Long userSenderId;
    @Column(name = "user_receiver_id")
    private Long userReceiverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsersKey)) return false;

        UsersKey usersKey = (UsersKey) o;

        if (!Objects.equals(userSenderId, usersKey.userSenderId))
            return false;
        return Objects.equals(userReceiverId, usersKey.userReceiverId);
    }

    @Override
    public int hashCode() {
        return userSenderId.hashCode() + userSenderId.hashCode();
    }
}
