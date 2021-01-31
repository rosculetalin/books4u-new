package com.example.springsocial.repository;

import com.example.springsocial.model.Chat;
import com.example.springsocial.model.compositeKey.UsersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UsersKey> {
}
