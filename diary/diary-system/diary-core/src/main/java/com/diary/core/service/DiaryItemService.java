package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.dto.ItemDTO;
import com.diary.core.entity.DiaryItem;
import com.diary.core.vo.ItemVO;

import java.time.LocalDate;
import java.util.List;

public interface DiaryItemService extends IService<DiaryItem> {

    List<ItemVO> listByDate(Long userId, LocalDate date);

    void create(Long userId, ItemDTO dto);

    void update(Long userId, Long id, ItemDTO dto);

    void toggle(Long userId, Long id);

    void delete(Long userId, Long id);
}
