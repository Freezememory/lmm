package com.wanglj.lmm.admin.controller;


import com.wanglj.lmm.admin.api.enums.AdminCode;
import com.wanglj.lmm.admin.feign.RemoteTestService;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import com.wanglj.lmm.common.base.enums.SecurityConstants;
import com.wanglj.lmm.common.base.util.R;
import com.wanglj.lmm.common.base.util.SimpleObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * qcx-test
 * @author Wanglj
 * @since 2023-02-07 14:43:01
 */
@Slf4j
@RestController
@RequestMapping(LmmConstants.ADMIN_URL_PREFIX + "/rec")
@RequiredArgsConstructor
public class TestFeignController {

    private final RemoteTestService remoteTestService;


    @PostMapping("/test1")
    public R<SimpleObject> test1(@RequestBody SimpleObject info) {
        R<SimpleObject> simpleObject = remoteTestService.info1(new SimpleObject(), SecurityConstants.FROM_IN);
        log.info("test1: {}", simpleObject.toString());
        return R.ok(AdminCode.Success, SimpleObject.build(simpleObject.toString()));
    }

    @PostMapping("/test2")
    public R<SimpleObject> test2(@RequestBody SimpleObject info) {
        R<SimpleObject> simpleObject = remoteTestService.info2(new SimpleObject(), SecurityConstants.FROM_IN);
        log.info("test1: {}", simpleObject.toString());
        return R.ok(AdminCode.Success, SimpleObject.build(simpleObject.toString()));
    }


}
