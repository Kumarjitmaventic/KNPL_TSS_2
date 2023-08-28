package com.knpl.tss.exceptions;

public class InvalidCsvHeaderException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    public InvalidCsvHeaderException(String message) {
        super(message);
    }
}
