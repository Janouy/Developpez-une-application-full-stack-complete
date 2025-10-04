package com.openclassrooms.mddapi.error;

public class AlreadySubscribedException extends RuntimeException {
    public AlreadySubscribedException(String message) { super(message); }
}
