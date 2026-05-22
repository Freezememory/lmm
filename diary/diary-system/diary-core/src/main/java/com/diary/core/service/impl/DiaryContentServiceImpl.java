package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.core.entity.DiaryContent;
import com.diary.core.mapper.DiaryContentMapper;
import com.diary.core.service.DiaryContentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DiaryContentServiceImpl extends ServiceImpl<DiaryContentMapper, DiaryContent> implements DiaryContentService {

    @Override
    public DiaryContent getByDate(Long userId, LocalDate date) {
        return this.getOne(new LambdaQueryWrapper<DiaryContent>()
                .eq(DiaryContent::getUserId, userId)
                .eq(DiaryContent::getDiaryDate, date));
    }

    @Override
    public void save(Long userId, LocalDate date, String textContent) {
        DiaryContent content = this.getByDate(userId, date);
        if (content == null) {
            content = new DiaryContent();
            content.setUserId(userId);
            content.setDiaryDate(date);
            content.setTextContent(textContent);
            content.setCreateTime(LocalDateTime.now());
            content.setUpdateTime(LocalDateTime.now());
            this.save(content);
        } else {
            content.setTextContent(textContent);
            content.setUpdateTime(LocalDateTime.now());
            this.updateById(content);
        }
    }
}
