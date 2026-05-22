package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.entity.DiaryImage;
import com.diary.core.service.DiaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diary/images")
@RequiredArgsConstructor
public class ImageController {

    private final DiaryImageService imageService;

    @Value("${diary.image.upload-path:./uploads/images}")
    private String uploadPath;

    @GetMapping
    public R<List<DiaryImage>> list(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(imageService.listByDate(userId, date));
    }

    @PostMapping("/upload")
    public R<DiaryImage> upload(@RequestHeader("X-User-Id") Long userId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;

        Path uploadDir = Paths.get(uploadPath);
        Files.createDirectories(uploadDir);
        Files.copy(file.getInputStream(), uploadDir.resolve(fileName));

        String imageUrl = "/uploads/images/" + fileName;
        DiaryImage image = imageService.upload(userId, date, imageUrl);
        return R.ok(image);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        imageService.delete(userId, id);
        return R.ok();
    }
}
