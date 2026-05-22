package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.service.DiaryCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final DiaryCategoryService categoryService;

    @GetMapping
    public R<List<DiaryCategory>> list(@RequestHeader("X-User-Id") Long userId) {
        return R.ok(categoryService.listByUserId(userId));
    }

    @PostMapping
    public R<Void> create(@RequestHeader("X-User-Id") Long userId,
                          @Valid @RequestBody CategoryDTO dto) {
        categoryService.create(userId, dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id,
                          @Valid @RequestBody CategoryDTO dto) {
        categoryService.update(userId, id, dto);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        categoryService.delete(userId, id);
        return R.ok();
    }
}
