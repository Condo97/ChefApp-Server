package com.pantrypro.common.exceptions;

public class AutoIncrementingDBObjectExistsException extends Exception {

    public AutoIncrementingDBObjectExistsException(String message) {
        super(message);
    }
}
