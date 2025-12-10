package com.miracle.src.models.exceptions;

//extend the exception
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

}


