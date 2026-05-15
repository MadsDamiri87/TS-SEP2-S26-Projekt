package com.example.backend.shared.exception;

public class InvalidFileException extends RuntimeException
{
    public InvalidFileException(String message)
    {
        super(message);
    }
}
