package com.platform.deck.card.service.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class FileTooLargeException extends RuntimeException{
    public String errorCode;
    public Map<String, Object> errorDetails;

    public FileTooLargeException(String message){
        super(message);
    }
}
