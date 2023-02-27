package com.wanglj.lmm.gateway.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.wanglj.lmm.common.base.util.AesUtil;
import com.wanglj.lmm.gateway.config.properties.GatewayConfigProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Optional;

/**
 * 微服务端的响应加密
 */
//@Component
@Slf4j
@RequiredArgsConstructor
public class GlobalResponseFilter implements GlobalFilter, Ordered {
    private final GatewayConfigProperties configProperties;

    //@Autowired
    private final ServerCodecConfigurer codecConfigurer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        //对post的请求体处理
        return chain.filter(exchange.mutate().response(decorate(exchange)).build());
    }

    ServerHttpResponse decorate(ServerWebExchange exchange) {
        String finalOriginalResponseContentType = MediaType.APPLICATION_JSON_VALUE;
        return new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Class<String> inClass = String.class;
                Class<String> outClass = String.class;

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(HttpHeaders.CONTENT_TYPE, finalOriginalResponseContentType);
                ClientResponse clientResponse = ClientResponse
                        .create(exchange.getResponse().getStatusCode(), codecConfigurer.getReaders())
                        .headers(headers -> headers.putAll(httpHeaders))
                        .body(Flux.from(body)).build();

                Mono<String> modifiedBody = clientResponse.bodyToMono(inClass)
                        .flatMap(originalBody -> {
                            String lastStr = originalBody.toString();
                            //修改response之后的字符串
                            log.info("加密前响应地址:{} 响应体:{}", exchange.getRequest().getURI().getRawPath(), lastStr);
                            if (StrUtil.containsIgnoreCase(lastStr, "\"code\":")
                                    && ((StrUtil.containsIgnoreCase(lastStr, "\"data\":") || StrUtil.containsIgnoreCase(lastStr, "\"page\":"))
                                    && JSONUtil.isJson(lastStr))) {

                                Object globalResponse = getGlobalResponse(lastStr);
                                if (globalResponse != null) {
                                    lastStr = JSONUtil.toJsonStr(globalResponse);
                                }
                            }
                            log.info("加密后响应地址:{} 响应体:{}", exchange.getRequest().getURI().getRawPath(), lastStr);
                            return Mono.just(lastStr);
                        });

                BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
                CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, exchange.getResponse().getHeaders());

                return bodyInserter.insert(outputMessage, new BodyInserterContext())
                        .then(Mono.defer(() -> {
                            Flux<DataBuffer> messageBody = outputMessage.getBody();
                            HttpHeaders headers = getDelegate().getHeaders();
                            if (!headers.containsKey(HttpHeaders.TRANSFER_ENCODING)) {
                                messageBody = messageBody.doOnNext(data -> headers.setContentLength(data.readableByteCount()));
                            }
                            return getDelegate().writeWith(messageBody);
                        }));
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };
    }

    @Override
    public int getOrder() {
        //必须小于默认过滤器 NettyWriteResponseFilter（-1）
        return -2;
    }


    /**
     * @param originalResponseStr 返回参数
     * @return 加密对象
     */
    private Object getGlobalResponse(String originalResponseStr) {
        try {
            JSONObject jsonObject = JSONUtil.parseObj(originalResponseStr, true, true);
            if (jsonObject.containsKey("code") && jsonObject.containsKey("data")) {
                GlobalResponse globalResponse = new GlobalResponse();
                globalResponse.setCode(jsonObject.getInt("code"));
                globalResponse.setMsg(Optional.ofNullable(jsonObject.getStr("msg")).orElse(""));
                String data = jsonObject.getStr("data");
                String dataEncrypt = StringUtils.isEmpty(data) ? data : AesUtil.encrypt(data, configProperties.getRespEncodeKey());
                globalResponse.setData(Optional.ofNullable(dataEncrypt).orElse(""));
                return globalResponse;
            }
            if (jsonObject.containsKey("code") && jsonObject.containsKey("page")) {
                GlobalResponsePage globalResponse = new GlobalResponsePage();
                globalResponse.setCode(jsonObject.getInt("code"));
                globalResponse.setMsg(Optional.ofNullable(jsonObject.getStr("msg")).orElse(""));
                String page = jsonObject.getObj("page").toString();
                String dataEncrypt = StringUtils.isEmpty(page) ? page : AesUtil.encrypt(page, configProperties.getRespEncodeKey());
                globalResponse.setPage(Optional.ofNullable(dataEncrypt).orElse(""));
                return globalResponse;
            }
        } catch (Exception e) {
            log.error("filter加密失败:  ", e);
        }
        return null;
    }

    /**
     * @description: 返回给前端的统一格式
     */
    @Data
    private static class GlobalResponse implements Serializable {
        private static final long serialVersionUID = 1L;

        private int code;

        private String msg = "";

        private Object data;
    }

    /**
     * @description: 返回给前端的统一分页格式
     */
    @Data
    private static class GlobalResponsePage implements Serializable {
        private static final long serialVersionUID = 1L;

        private int code;

        private String msg = "";

        private Object page;
    }
}

