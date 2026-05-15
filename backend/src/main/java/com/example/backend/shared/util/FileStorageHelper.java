package com.example.backend.shared.util;

import com.example.backend.shared.exception.InvalidFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileStorageHelper
{
    public static void deletePhysicalFile(String filePath)
    {
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException exception) {
            throw new InvalidFileException("Could not delete file");
        }
    }
}
