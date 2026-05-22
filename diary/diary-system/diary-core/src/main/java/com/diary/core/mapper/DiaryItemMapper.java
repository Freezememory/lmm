package com.diary.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.core.entity.DiaryItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryItemMapper extends BaseMapper<DiaryItem> {
}
