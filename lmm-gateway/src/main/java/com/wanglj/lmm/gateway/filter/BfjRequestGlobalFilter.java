package com.wanglj.lmm.gateway.filter;

import com.wanglj.lmm.common.base.enums.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * <p>
 * 全局拦截器，作用所有的微服务
 * <p>
 * 1. 对请求头中参数进行处理 from 参数进行清洗 2. 重写StripPrefix = 1,支持全局
 * <p>
 * 支持swagger添加X-Forwarded-Prefix header （F SR2 已经支持，不需要自己维护）
 */
@Slf4j
@Component
public class BfjRequestGlobalFilter implements GlobalFilter, Ordered {

    /**
     * Process the Web request and (optionally) delegate to the next {@code WebFilter}
     * through the given {@link GatewayFilterChain}.
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return {@code Mono<Void>} to indicate when request processing is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. 清洗请求头中from 参数
        ServerHttpRequest request = exchange.getRequest().mutate()
                .headers(httpHeaders -> httpHeaders.remove(SecurityConstants.FROM)).build();
        //链路追踪id  远程调用token传递
        //String traceId = IdUtils.getNextIdStr();
        //request.mutate().header(CommonConstants.TRACE_ID, traceId);
        //MDC.put(CommonConstants.TRACE_ID, traceId);

        // 2. 重写StripPrefix
        //addOriginalRequestUrl(exchange, request.getURI());
        String rawPath = request.getURI().getRawPath();

        log.info("请求地址:{}", rawPath);
        /*
        String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/")).skip(1L)
                .collect(Collectors.joining("/"));
        ServerHttpRequest newRequest = request.mutate().path(rawPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
         */
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1000;
    }

}
