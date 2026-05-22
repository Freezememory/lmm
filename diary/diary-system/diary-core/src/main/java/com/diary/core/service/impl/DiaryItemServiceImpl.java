package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.dto.ItemDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.entity.DiaryItem;
import com.diary.core.mapper.DiaryItemMapper;
import com.diary.core.service.DiaryCategoryService;
import com.diary.core.service.DiaryItemService;
import com.diary.core.vo.ItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryItemServiceImpl extends ServiceImpl<DiaryItemMapper, DiaryItem> implements DiaryItemService {

    private final DiaryCategoryService categoryService;

    @Override
    public List<ItemVO> listByDate(Long userId, LocalDate date) {
        List<DiaryItem> items = this.list(new LambdaQueryWrapper<DiaryItem>()
                .eq(DiaryItem::getUserId, userId)
                .eq(DiaryItem::getDiaryDate, date)
                .orderByAsc(DiaryItem::getSortOrder));

        List<DiaryCategory> categories = categoryService.listByUserId(userId);
        Map<Long, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(DiaryCategory::getId, DiaryCategory::getName));

        return items.stream().map(item -> ItemVO.builder()
                .id(item.getId())
                .categoryId(item.getCategoryId())
                .categoryName(categoryMap.get(item.getCategoryId()))
                .diaryDate(item.getDiaryDate())
                .content(item.getContent())
                .isDone(item.getIsDone())
                .sortOrder(item.getSortOrder())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void create(Long userId, ItemDTO dto) {
        DiaryItem item = new DiaryItem();
        item.setUserId(userId);
        item.setCategoryId(dto.getCategoryId());
        item.setDiaryDate(dto.getDiaryDate());
        item.setContent(dto.getContent());
        item.setIsDone(0);
        item.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 99);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        this.save(item);
    }

    @Override
    public void update(Long userId, Long id, ItemDTO dto) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        item.setCategoryId(dto.getCategoryId());
        item.setContent(dto.getContent());
        if (dto.getSortOrder() != null) {
            item.setSortOrder(dto.getSortOrder());
        }
        item.setUpdateTime(LocalDateTime.now());
        this.updateById(item);
    }

    @Override
    public void toggle(Long userId, Long id) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        item.setIsDone(item.getIsDone() == 1 ? 0 : 1);
        item.setUpdateTime(LocalDateTime.now());
        this.updateById(item);
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        this.removeById(id);
    }
}
