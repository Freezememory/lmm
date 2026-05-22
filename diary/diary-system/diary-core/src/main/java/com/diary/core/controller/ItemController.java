package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.dto.ItemDTO;
import com.diary.core.service.DiaryItemService;
import com.diary.core.vo.ItemVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary/items")
@RequiredArgsConstructor
public class ItemController {

    private final DiaryItemService itemService;

    @GetMapping
    public R<List<ItemVO>> list(@RequestHeader("X-User-Id") Long userId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(itemService.listByDate(userId, date));
    }

    @PostMapping
    public R<Void> create(@RequestHeader("X-User-Id") Long userId,
                          @Valid @RequestBody ItemDTO dto) {
        itemService.create(userId, dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id,
                          @Valid @RequestBody ItemDTO dto) {
        itemService.update(userId, id, dto);
        return R.ok();
    }

    @PutMapping("/{id}/toggle")
    public R<Void> toggle(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        itemService.toggle(userId, id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        itemService.delete(userId, id);
        return R.ok();
    }
}
