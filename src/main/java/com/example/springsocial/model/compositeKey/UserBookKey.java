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
public class UserBookKey implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "book_id")
    private Long bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserBookKey)) return false;

        UserBookKey userBookKey = (UserBookKey) o;

        if (!Objects.equals(userId, userBookKey.getUserId()))
            return false;
        return Objects.equals(bookId, userBookKey.getBookId());
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + bookId.hashCode();
    }
}
