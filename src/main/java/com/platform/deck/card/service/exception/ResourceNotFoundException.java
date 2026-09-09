package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ResourceNotFoundException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public ResourceNotFoundException(String message, String userId){
        super(message);
    }

    public ResourceNotFoundException(String message, Long userId) {
    }

    public ResourceNotFoundException(String format) {
    }
}
