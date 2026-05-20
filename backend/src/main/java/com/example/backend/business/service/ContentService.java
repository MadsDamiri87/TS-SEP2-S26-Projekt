package com.example.backend.business.service;

import com.example.backend.business.dto.content.ContentResponse;
import com.example.backend.business.dto.content.ContentType;
import com.example.backend.business.dto.mapper.ContentMapper;
import com.example.backend.entity.Content;
import com.example.backend.entity.Lesson;
import com.example.backend.persistence.repository.ContentRepository;
import com.example.backend.persistence.repository.LessonRepository;
import com.example.backend.shared.exception.EmptyFileException;
import com.example.backend.shared.exception.InvalidFileException;
import com.example.backend.shared.exception.ResourceNotFoundException;
import com.example.backend.shared.util.FileStorageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ContentService
{
    private final ContentRepository contentRepository;
    private final LessonRepository lessonRepository;
    private final ContentMapper contentMapper;
    private final Path uploadRoot;
  
    private final Path uploadRoot = Paths.get("seedData", "lesson-content");
  
    public ContentService(
            ContentRepository contentRepository,
            LessonRepository lessonRepository,
            ContentMapper contentMapper,

            @Value("${app.upload-root:data/lesson-content}")
            String uploadRoot
    )
    {
        this.contentRepository = contentRepository;
        this.lessonRepository = lessonRepository;
        this.contentMapper = contentMapper;
        this.uploadRoot = Paths.get(uploadRoot);
    }

    @Transactional
    public ContentResponse upload(Long lessonId, MultipartFile file)
    {
        Lesson lesson = getLesson(lessonId);

        if (file.isEmpty()) throw new EmptyFileException();

        ContentType contentType = determineContentType(file);

        try
        {
            Files.createDirectories(uploadRoot.resolve("lesson-" + lessonId));

            String originalFileName = file.getOriginalFilename();
            String safeFileName = UUID.randomUUID() + "-" + originalFileName;

            Path filePath = uploadRoot
                    .resolve("lesson-" + lessonId)
                    .resolve(safeFileName)
                    .normalize();

            file.transferTo(filePath);

            int nextOrderNumber = contentRepository.findHighestOrderNumberByLessonId(lessonId) + 1;

            Content content = new Content();
            content.setLesson(lesson);
            content.setOriginalFileName(originalFileName);
            content.setFilePath(filePath.toString());
            content.setContentType(contentType);
            content.setOrderNumber(nextOrderNumber);

            Content savedContent = contentRepository.save(content);

            return contentMapper.toResponse(savedContent);

        } catch (Exception exception) {
            throw new InvalidFileException("Could not upload file, something went wrong");
        }
    }

    public ContentResponse getById(Long contentId)
    {
        Content content = getContent(contentId);
        return contentMapper.toResponse(content);
    }

    public List<ContentResponse> getAllByLessonId(Long lessonId)
    {
        List<Content> contents = contentRepository.findAllByLesson_LessonIdOrderByOrderNumberAsc(lessonId);
        return contentMapper.toResponse(contents);
    }

    public ResponseEntity<Resource> getFile(Long contentId)
    {
        Content content = getContent(contentId);

        try {
            Path path = Paths.get(content.getFilePath()).normalize();
            Resource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("File could not be read");
            }

            return ResponseEntity.ok()
                    .contentType(getMediaType(content))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + content.getOriginalFileName() + "\""
                    )
                    .body(resource);
        }
        catch (MalformedURLException exception) {
            throw new RuntimeException("Could not read file", exception);
        }
    }

    @Transactional
    public void delete(Long contentId)
    {
        Content content = getContent(contentId);

        Long lessonId = content.getLesson().getLessonId();
        int deletedOrderNumber = content.getOrderNumber();
        String filePath = content.getFilePath();

        FileStorageHelper.deletePhysicalFile(filePath);

        contentRepository.delete(content);
        contentRepository.flush();

        List<Content> contentsToShift = contentRepository
                .findAllByLesson_LessonIdAndOrderNumberGreaterThan(lessonId, deletedOrderNumber);

        for (Content contentToShift : contentsToShift) {
            contentToShift.setOrderNumber(contentToShift.getOrderNumber() - 1);
        }

        contentRepository.saveAll(contentsToShift);
    }

    private Lesson getLesson(Long lessonId)
    {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("No lesson found with id=" + lessonId));
    }

    private ContentType determineContentType(MultipartFile file)
    {
        String contentType = file.getContentType();

        if (contentType == null) {
            throw new InvalidFileException("File type is missing");
        }

        if (contentType.equals("text/markdown") || contentType.equals("text/plain")) {
            return ContentType.MARKDOWN;
        }

        if (contentType.startsWith("image/")) {
            return ContentType.IMAGE;
        }

        if (contentType.startsWith("video/")) {
            return ContentType.VIDEO;
        }

        throw new InvalidFileException("Unsupported file type: " + contentType);
    }

    private MediaType getMediaType(Content content)
    {
        return switch (content.getContentType()) {
            case MARKDOWN -> MediaType.TEXT_PLAIN;
            case IMAGE -> MediaType.IMAGE_PNG;
            case VIDEO -> MediaType.valueOf("video/mp4");
        };
    }

    private Content getContent(Long contentId)
    {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("No content found with id=" + contentId));
    }
}