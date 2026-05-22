package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.entity.DiaryImage;

import java.time.LocalDate;
import java.util.List;

public interface DiaryImageService extends IService<DiaryImage> {

    List<DiaryImage> listByDate(Long userId, LocalDate date);

    DiaryImage upload(Long userId, LocalDate date, String imageUrl);

    void delete(Long userId, Long id);
}
