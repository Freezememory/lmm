package com.wanglj.lmm.common.security.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.wanglj.lmm.common.security.component.PermitAllUrlProperties;
import com.wanglj.lmm.common.security.util.SpringContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 全局路由鉴权
 * @author: Wanglj
 * @date: 2023/2/17 21:51
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    static List<String> list = new ArrayList<>();

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册 Sa-Token 拦截器，定义详细认证规则
        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) -> {
            // 指定一条 match 规则
            // 拦截的 path 列表，可以写多个 */
            SaRouter.match("/**")
                    // 排除掉的 path 列表，可以写多个
                    .notMatch("/auth/login")
                    .notMatch(getIgnoreList())
                    // 要执行的校验动作，可以写完整的 lambda 表达式
                    .check(r -> StpUtil.checkLogin());

            // 根据路由划分模块，不同模块不同鉴权
            //SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
        })).addPathPatterns("/**");
    }

    /**
     * 白名单过滤
     */
    private List<String> getIgnoreList() {
        if (CollectionUtil.isEmpty(list)) {
            PermitAllUrlProperties properties = SpringContextHolder.getBean(PermitAllUrlProperties.class);
            list.addAll(properties.getUrls());
        }
        return list;
    }

}
