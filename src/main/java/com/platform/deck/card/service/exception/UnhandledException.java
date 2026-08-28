package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UnhandledException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public UnhandledException(String message){
        super(message);
    }
}
