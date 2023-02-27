/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wanglj.lmm.common.security.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.wanglj.lmm.common.base.LoginUser;
import com.wanglj.lmm.common.base.enums.SecurityConstants;
import com.wanglj.lmm.common.base.exception.BaseException;
import com.wanglj.lmm.common.satoken.utils.LoginHelper;
import com.wanglj.lmm.common.security.util.SpringContextHolder;
import com.wanglj.lmm.common.security.annotation.Lmmer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.wanglj.lmm.common.base.enums.FrameCode.*;

/**
 * @author lengleng
 * @date 2019/02/14
 * <p>
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class BfjSecurityInnerAspect implements Ordered {

    private final HttpServletRequest request;

    static List<String> list = new ArrayList<>();

    @SneakyThrows
    @Around("@within(lmmer) || @annotation(lmmer)")
    public Object around(ProceedingJoinPoint point, Lmmer lmmer) {
        // 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
        if (lmmer == null) {
            Class<?> clazz = point.getTarget().getClass();
            lmmer = AnnotationUtils.findAnnotation(clazz, Lmmer.class);
        }

        String url = request.getRequestURI();
        String header = request.getHeader(SecurityConstants.FROM);

        if (lmmer.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("访问接口 {} 没有权限 FROM_IN {}", url, header);
            //throw new BaseException("Access is denied");
            throw new BaseException(InterInterfaceFailed.getCode(), InterInterfaceFailed.getMsg());
        } else if (!lmmer.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            if (CollectionUtil.isEmpty(list)) {
                PermitAllUrlProperties properties = SpringContextHolder.getBean(PermitAllUrlProperties.class);
                list.addAll(properties.getOriginalUrls());
            }
            boolean has = false;
            for (String x : list) {
                AntPathMatcher antPathMatcher = new AntPathMatcher();
                if (antPathMatcher.match(x, url) || url.equalsIgnoreCase(x)) {
                    has = true;
                    break;
                }
            }
            LoginUser loginUser = LoginHelper.getLoginUser();
            if (!has && loginUser == null) {
                log.warn("访问接口 {} 没有权限", url);
                throw new BaseException(AuthenticationFailed.getCode(), AuthenticationFailed.getMsg());
            }
        } else if (StrUtil.equals(SecurityConstants.FROM_IN, header)) {
            log.warn("内部接口访问接口 {}", url);
        }
        return point.proceed();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

}
