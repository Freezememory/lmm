package com.wanglj.lmm.portal.controller;


import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import com.wanglj.lmm.common.security.annotation.Lmmer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 互认信息查询管理
 * @author Wanglj
 * @since 2023-02-07 14:43:01
 */
@Slf4j
@RestController
@RequestMapping(LmmConstants.PORTAL_URL_PREFIX + "/test")
@RequiredArgsConstructor
public class TestController {


    /**
     * 添加检查、检验报告信息
     * @return: Boolean
     */
    @Lmmer
    @PostMapping("/info1")
    public R<SimpleObject> info1(@RequestBody SimpleObject info) {
        log.info("进入 info1  方法");
        return R.ok(200, null, SimpleObject.build("info1"));
    }

    @Lmmer(value = false)
    @PostMapping("/info2")
    public R<SimpleObject> info2(@RequestBody SimpleObject info) {
        log.info("进入 info2  方法");
        return R.ok(200, null, SimpleObject.build("info2"));
    }


}
