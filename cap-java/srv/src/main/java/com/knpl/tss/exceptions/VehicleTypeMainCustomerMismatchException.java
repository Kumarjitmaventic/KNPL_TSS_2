package com.knpl.tss.exceptions;

public class VehicleTypeMainCustomerMismatchException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;
    
    public VehicleTypeMainCustomerMismatchException(String message) {
        super(message);
    }
}
