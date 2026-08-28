package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DuplicateResourceException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public DuplicateResourceException(String message){
        super(message);
    }
}
