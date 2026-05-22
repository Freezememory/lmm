package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("diary_category")
public class DiaryCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private Integer sortOrder;

    private Integer isPreset;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
