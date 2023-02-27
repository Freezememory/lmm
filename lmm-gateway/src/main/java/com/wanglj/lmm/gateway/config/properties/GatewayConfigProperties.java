package com.wanglj.lmm.gateway.config.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wanglj
 * @date 2023/2/15
 * 网关配置文件
 */
@Data
@NoArgsConstructor
@Configuration
@RefreshScope
@ConfigurationProperties("gateway")
public class GatewayConfigProperties {

	/**
	 * 前端请求加密的key
	 */
	public String reqEncodeKey;

	/**
	 * 后端响应加密的key
	 */
	public String respEncodeKey;

}