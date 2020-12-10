package com.example.demo.Exceptions;

public class OutOfStockException extends Exception{
    public OutOfStockException(String errorMessage){
        super(errorMessage);
    }
}
