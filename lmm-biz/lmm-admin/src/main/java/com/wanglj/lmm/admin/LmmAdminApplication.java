package com.wanglj.lmm.admin;

import com.wanglj.lmm.common.base.constant.BfjConstants;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = {BfjConstants.MAPPER_SCAN})
public class LmmAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmmAdminApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
