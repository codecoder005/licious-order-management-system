package com.licious.oms.exception;

public class ItemOutOfStockException extends InSufficientItemsInStockException{
    public ItemOutOfStockException(String message) {
        super(message);
    }
}
