package com.example.springsocial.repository;

import com.example.springsocial.model.UserBook;
import com.example.springsocial.model.compositeKey.UserBookKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, UserBookKey> {
}
