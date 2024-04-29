package com.licious.oms.exception;

public class InSufficientItemsInStockException extends RuntimeException {
    public InSufficientItemsInStockException(String message) {
        super(message);
    }
}
