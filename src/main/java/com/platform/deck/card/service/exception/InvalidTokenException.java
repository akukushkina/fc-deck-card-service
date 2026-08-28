package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class InvalidTokenException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public InvalidTokenException(String message){
        super(message);
    }
}
