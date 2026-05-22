package com.diary.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemDTO {

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "日记日期不能为空")
    private LocalDate diaryDate;

    @NotBlank(message = "内容不能为空")
    @Size(max = 500, message = "内容最长500个字符")
    private String content;

    private Integer sortOrder;
}
