package com.example.backend.shared.exception;

public class EmptyFileException extends RuntimeException
{
    public EmptyFileException()
    {
        super("File cannot be empty");
    }
}
