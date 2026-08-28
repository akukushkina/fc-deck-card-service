package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class RateLimitExceededException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public RateLimitExceededException(String message){
        super(message);
    }
}
