package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.entity.DiaryImage;
import com.diary.core.mapper.DiaryImageMapper;
import com.diary.core.service.DiaryImageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryImageServiceImpl extends ServiceImpl<DiaryImageMapper, DiaryImage> implements DiaryImageService {

    @Override
    public List<DiaryImage> listByDate(Long userId, LocalDate date) {
        return this.list(new LambdaQueryWrapper<DiaryImage>()
                .eq(DiaryImage::getUserId, userId)
                .eq(DiaryImage::getDiaryDate, date)
                .orderByAsc(DiaryImage::getSortOrder));
    }

    @Override
    public DiaryImage upload(Long userId, LocalDate date, String imageUrl) {
        DiaryImage image = new DiaryImage();
        image.setUserId(userId);
        image.setDiaryDate(date);
        image.setImageUrl(imageUrl);
        image.setSortOrder(99);
        image.setCreateTime(LocalDateTime.now());
        this.save(image);
        return image;
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryImage image = this.getById(id);
        if (image == null || !image.getUserId().equals(userId)) {
            throw new BusinessException("图片不存在");
        }
        this.removeById(id);
    }
}
