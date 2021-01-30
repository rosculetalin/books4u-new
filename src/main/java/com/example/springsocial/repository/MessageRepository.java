package com.example.springsocial.repository;

import com.example.springsocial.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByOrderByDatetimeAsc();
    List<Message> findAllByOrderByDatetimeDesc();
}
