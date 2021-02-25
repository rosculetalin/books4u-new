package com.example.springsocial.repository;

import com.example.springsocial.model.Post;
import com.example.springsocial.model.PostForBook;
import com.example.springsocial.model.User;
import com.example.springsocial.model.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostForBookRepository extends JpaRepository<PostForBook, Long> {
    List<PostForBook> findAllByUserBook(UserBook userBook);

    List<PostForBook> findAllByUserBookIn(List<UserBook> userBookList);
}
