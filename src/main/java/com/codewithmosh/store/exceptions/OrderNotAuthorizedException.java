package com.codewithmosh.store.exceptions;

public class OrderNotAuthorizedException extends RuntimeException {
    public OrderNotAuthorizedException() {
        super("This order belongs to another user");
    }
}
