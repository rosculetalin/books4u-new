package com.example.springsocial.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageConversationDto {
    private String datetime;
    private Boolean isCurrent;
    private String messageValue;
}
