package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.entity.DiaryContent;

import java.time.LocalDate;

public interface DiaryContentService extends IService<DiaryContent> {

    DiaryContent getByDate(Long userId, LocalDate date);

    void save(Long userId, LocalDate date, String textContent);
}
