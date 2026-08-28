package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public ValidationException(String message){
        super(message);
    }
}
