package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary_content")
public class DiaryContent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate diaryDate;

    private String textContent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
