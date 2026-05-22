package com.diary.core.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ItemVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private LocalDate diaryDate;
    private String content;
    private Integer isDone;
    private Integer sortOrder;
}
