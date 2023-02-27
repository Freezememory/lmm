package com.wanglj.lmm.common.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisConfigProperties {

	private String password;

	private String database;

	private Integer port;

	private String host;

}
