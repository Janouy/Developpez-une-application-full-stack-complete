package com.openclassrooms.mddapi.error;

public class SubjectNotFoundException extends RuntimeException {
    public SubjectNotFoundException(String message) {
        super(message);
    }
}
