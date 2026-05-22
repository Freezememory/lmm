package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary_image")
public class DiaryImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate diaryDate;

    private String imageUrl;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
