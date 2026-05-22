package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.mapper.DiaryCategoryMapper;
import com.diary.core.service.DiaryCategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryCategoryServiceImpl extends ServiceImpl<DiaryCategoryMapper, DiaryCategory> implements DiaryCategoryService {

    @Override
    public List<DiaryCategory> listByUserId(Long userId) {
        LambdaQueryWrapper<DiaryCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryCategory::getUserId, userId)
                .orderByAsc(DiaryCategory::getSortOrder);
        return this.list(wrapper);
    }

    @Override
    public void create(Long userId, CategoryDTO dto) {
        long count = this.count(new LambdaQueryWrapper<DiaryCategory>()
                .eq(DiaryCategory::getUserId, userId)
                .eq(DiaryCategory::getName, dto.getName()));
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }

        DiaryCategory category = new DiaryCategory();
        category.setUserId(userId);
        category.setName(dto.getName());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 99);
        category.setIsPreset(0);
        category.setCreateTime(LocalDateTime.now());
        this.save(category);
    }

    @Override
    public void update(Long userId, Long id, CategoryDTO dto) {
        DiaryCategory category = this.getById(id);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }
        if (category.getIsPreset() == 1) {
            throw new BusinessException("预设分类不可修改");
        }
        category.setName(dto.getName());
        if (dto.getSortOrder() != null) {
            category.setSortOrder(dto.getSortOrder());
        }
        this.updateById(category);
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryCategory category = this.getById(id);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }
        if (category.getIsPreset() == 1) {
            throw new BusinessException("预设分类不可删除");
        }
        this.removeById(id);
    }
}
