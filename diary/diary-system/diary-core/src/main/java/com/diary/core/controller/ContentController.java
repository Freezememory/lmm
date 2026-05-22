package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.dto.ContentDTO;
import com.diary.core.entity.DiaryContent;
import com.diary.core.service.DiaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diary/content")
@RequiredArgsConstructor
public class ContentController {

    private final DiaryContentService contentService;

    @GetMapping
    public R<DiaryContent> get(@RequestHeader("X-User-Id") Long userId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(contentService.getByDate(userId, date));
    }

    @PutMapping
    public R<Void> save(@RequestHeader("X-User-Id") Long userId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestBody ContentDTO dto) {
        contentService.save(userId, date, dto.getTextContent());
        return R.ok();
    }
}
