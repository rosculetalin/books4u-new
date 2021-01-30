package com.example.springsocial.repository;

import com.example.springsocial.model.UserRelationship;
import com.example.springsocial.model.compositeKey.UserRelationshipKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRelationshipRepository extends JpaRepository<UserRelationship, UserRelationshipKey> {
}
