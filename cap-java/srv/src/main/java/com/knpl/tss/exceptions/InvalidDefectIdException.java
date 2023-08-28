package com.knpl.tss.exceptions;

public class InvalidDefectIdException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public InvalidDefectIdException(String message) {
        super(message);
    }
}