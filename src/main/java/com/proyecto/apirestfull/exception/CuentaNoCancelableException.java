package com.proyecto.apirestfull.exception;

public class CuentaNoCancelableException extends RuntimeException {
    public CuentaNoCancelableException(String message) {
        super(message);
    }
}
