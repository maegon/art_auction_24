package com.example.ArtAuction_24.global.customException;

public class CustomDuplicateEmailException extends RuntimeException {

    public CustomDuplicateEmailException(String message) {
        super(message);
    }

    public CustomDuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}