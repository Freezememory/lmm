package com.wanglj.lmm.auth;

import cn.dev33.satoken.SaManager;
import com.wanglj.lmm.common.base.constant.LmmConstants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = LmmConstants.BASE_PACKAGE)
public class LmmAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmmAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  授权认证模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
        System.out.println("Sa-Token配置如下：" + SaManager.getConfig());
    }
}
