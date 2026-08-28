package com.platform.deck.card.service.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter

public class BadCredentialException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;
    @Builder
    public BadCredentialException(String message){
        super(message);
    }
}
