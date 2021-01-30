package com.example.springsocial.model.compositeKey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationshipKey implements Serializable {
    @Column(name = "user_sender_id")
    private Long userSenderId;
    @Column(name = "user_receiver_id")
    private Long userReceiverId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRelationshipKey)) return false;

        UserRelationshipKey userRelationshipKey = (UserRelationshipKey) o;

        if (!Objects.equals(userSenderId, userRelationshipKey.userSenderId))
            return false;
        return Objects.equals(userReceiverId, userRelationshipKey.userReceiverId);
    }

    @Override
    public int hashCode() {
        return userSenderId.hashCode() + userSenderId.hashCode();
    }
}
