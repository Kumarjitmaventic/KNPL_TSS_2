package com.knpl.tss.exceptions;

public class CategorySubCategoryMismatchException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public CategorySubCategoryMismatchException(String message) {
        super(message);
    }
}
