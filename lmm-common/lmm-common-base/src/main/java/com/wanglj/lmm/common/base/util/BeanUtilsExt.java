package com.wanglj.lmm.common.base.util;

import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * BeanUtil扩展
 */
public class BeanUtilsExt {
    /**
     * 通过一个字节码拷贝一个新的List
     *
     * @param sourceList
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> Optional<List<T>> copyList(List<?> sourceList, Class<T> targetClass) {
        List<T> list = Optional.ofNullable(sourceList).orElse(Arrays.asList())
                .stream().map((ele -> convent(ele, targetClass)))
                .collect(Collectors.toList());
        return Optional.ofNullable(list);
    }

    /**
     * 通过一个字节码拷贝一个新的对象
     *
     * @param source
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> Optional<T> copyBean(Object source, Class<T> targetClass) {
        return Optional.ofNullable(convent(source, targetClass));
    }

    private static <T> T convent(Object source, Class<T> targetClass) {
        T t = null;
        try {
            t = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (source != null) {
            BeanUtils.copyProperties(source, t);
        }
        return t;
    }
}
