package com.example.backend.shared.util;

import com.example.backend.entity.Content;
import com.example.backend.shared.exception.InvalidFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    public static void deleteLessonContentFilesAndFolder(List<Content> contents)
    {
        Path lessonDirectory = null;

        for (Content content : contents) {
            Path filePath = Path.of(content.getFilePath());

            if (lessonDirectory == null) {
                lessonDirectory = filePath.getParent();
            }

            deletePhysicalFile(content.getFilePath());
        }

        deleteDirectoryIfEmpty(lessonDirectory);
    }

    public static void deleteDirectoryIfEmpty(Path directoryPath)
    {
        if (directoryPath == null) {
            return;
        }

        try {
            Files.deleteIfExists(directoryPath);
        } catch (IOException ignored) {
            // Folder was probably not empty.
            // We do not want the whole delete operation to fail because of folder cleanup.
        }
    }
}