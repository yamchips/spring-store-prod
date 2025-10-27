package com.codewithmosh.store.orders;

public class OrderNotAuthorizedException extends RuntimeException {
    public OrderNotAuthorizedException() {
        super("This order belongs to another user");
    }
}
