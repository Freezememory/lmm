package com.wanglj.lmm.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LmmPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmmPortalApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  门户模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
