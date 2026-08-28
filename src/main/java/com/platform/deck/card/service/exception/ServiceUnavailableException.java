package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class ServiceUnavailableException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public ServiceUnavailableException(String message){
        super(message);
    }
}
