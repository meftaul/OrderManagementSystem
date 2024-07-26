package com.meftaul.order.exception;

public class OrderFailedException extends RuntimeException {

    public OrderFailedException(String message) {
        super(message);
    }
}
