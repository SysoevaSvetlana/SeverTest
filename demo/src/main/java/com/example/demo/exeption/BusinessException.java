package com.example.demo.exeption;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}