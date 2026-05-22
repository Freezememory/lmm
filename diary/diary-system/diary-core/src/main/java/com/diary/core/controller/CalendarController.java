package com.diary.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diary.common.core.domain.R;
import com.diary.core.entity.DiaryContent;
import com.diary.core.service.DiaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final DiaryContentService contentService;

    @GetMapping
    public R<List<LocalDate>> getDiaryDates(@RequestHeader("X-User-Id") Long userId,
                                            @RequestParam int year,
                                            @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DiaryContent> contents = contentService.list(new LambdaQueryWrapper<DiaryContent>()
                .eq(DiaryContent::getUserId, userId)
                .between(DiaryContent::getDiaryDate, startDate, endDate)
                .select(DiaryContent::getDiaryDate));

        List<LocalDate> dates = contents.stream()
                .map(DiaryContent::getDiaryDate)
                .distinct()
                .collect(Collectors.toList());

        return R.ok(dates);
    }
}
