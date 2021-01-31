package com.example.springsocial.dtos;

import com.example.springsocial.model.RelationshipStatus;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRelationshipDto {
    @NotNull
    Long senderId;
    @NotNull
    Long receiverId;
    @NotNull
    RelationshipStatus relationshipStatus;
}
