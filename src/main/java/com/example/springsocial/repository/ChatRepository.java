package com.example.springsocial.repository;

import com.example.springsocial.model.Chat;
import com.example.springsocial.model.compositeKey.ChatKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, ChatKey> {
}
