package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ForbiddenException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public ForbiddenException(String message){
        super(message);
    }
}
