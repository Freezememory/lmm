package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;

import java.util.List;

public interface DiaryCategoryService extends IService<DiaryCategory> {

    List<DiaryCategory> listByUserId(Long userId);

    void create(Long userId, CategoryDTO dto);

    void update(Long userId, Long id, CategoryDTO dto);

    void delete(Long userId, Long id);
}
