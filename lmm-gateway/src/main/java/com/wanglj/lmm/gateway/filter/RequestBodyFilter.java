package com.wanglj.lmm.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.common.utils.StringUtils;
import com.wanglj.lmm.common.base.enums.FrameCode;
import com.wanglj.lmm.common.base.exception.BaseException;
import com.wanglj.lmm.common.base.util.AesUtil;
import com.wanglj.lmm.gateway.config.properties.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 请求体的封装处理
 */
//@Component
@Slf4j
@RequiredArgsConstructor
public class RequestBodyFilter implements GlobalFilter, Ordered {

    private final GatewayConfigProperties configProperties;
    private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    //private final RedisUtil redisUtil;
    public static ThreadLocal<String> local = new ThreadLocal<>();

    private final static String DEBUG = "debug";

    /**
     * 参数是否加密  1-开发模式不加密
     */
    private final static String DEBUG_TYPE = "1";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String url = exchange.getRequest().getURI().getRawPath();
        String query = exchange.getRequest().getURI().getQuery();
        log.info("请求url:{}", url);
        String debug = request.getHeaders().getFirst(DEBUG);

        //对post的请求体处理
        if (request.getMethod() == HttpMethod.POST && !DEBUG_TYPE.equals(debug)
                && MediaType.APPLICATION_JSON.isCompatibleWith(request.getHeaders().getContentType())) {
            log.info("开始加解密处理");
            return operationExchange(exchange, chain);
        } else if (request.getMethod() == HttpMethod.GET && StringUtils.isNotEmpty(query)) {
            return Mono.error(new BaseException(FrameCode.GetMethodParamFailed.getCode(), FrameCode.GetMethodParamFailed.getMsg()));
        } else {
            return chain.filter(exchange);
        }
    }

    private Mono<Void> operationExchange(ServerWebExchange exchange, GatewayFilterChain chain) {
        MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
        exchange.getAttributes().put("startTime", System.currentTimeMillis());
        // read & modify body
        ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            if (MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) {
                // 对原先的body进行操作，处理解密，添加参数等等
                JSONObject jsonObject = null;
                String data = null;
                if (!"{}".equals(body)) {
                    try {
                        String url = exchange.getRequest().getURI().getRawPath();
                        log.info("url:{} 解密前 body:{}", url, body);

                        data = JSON.parseObject(body).getString("data");
                        if (StringUtils.isBlank(data)) {
                            return Mono.error(new BaseException(FrameCode.RequestParamFailed.getCode(), FrameCode.RequestParamFailed.getMsg()));
                        }

                        String decryptStr = AesUtil.decrypt(data, configProperties.getReqEncodeKey());
                        if (StringUtils.isBlank(decryptStr)) {
                            log.error("数据解密异常:{} 解密数据为空", data);
                            return Mono.error(new BaseException(FrameCode.RequestParamFailed.getCode(), FrameCode.RequestParamFailed.getMsg()));
                            //return bufferFactory.wrap(objectMapper.writeValueAsBytes(R.fail(FrameCode.SystemInnerError.getCode(), ex.getMessage(), null)));
                        }
                        log.info("url:{} 解密后 body:{}", url, decryptStr);

                        jsonObject = JSON.parseObject(decryptStr);
                    } catch (Exception e) {
                        log.error("数据解密异常:  ", e);
                        return Mono.error(new BaseException(FrameCode.RequestParamFailed.getCode(), FrameCode.RequestParamFailed.getMsg()));
                    }
                }

                //将处理后的数据保存到属性里，后面业务逻辑使用
                if (StringUtils.isEmpty(data)) {
                    local.set(body);
                    return Mono.just(body);
                } else if (null != jsonObject) {
                    local.set(jsonObject.toJSONString());
                    return Mono.just(jsonObject.toJSONString());
                } else {
                    local.set("{}");
                    return Mono.just("{}");
                }
            }
            return Mono.empty();
        });
        BodyInserter<Mono<String>, org.springframework.http.ReactiveHttpOutputMessage> bodyInserter
                = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public HttpHeaders getHeaders() {
                    //对请求头修改，可以添加请求头参数等等
                    long contentLength = headers.getContentLength();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.putAll(super.getHeaders());
                    if (contentLength > 0) {
                        httpHeaders.setContentLength(contentLength);
                    } else {
                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                    }
                    return httpHeaders;
                }

                @Override
                public Flux<DataBuffer> getBody() {
                    return outputMessage.getBody();
                }
            };

            return returnMono(chain, exchange.mutate().request(decorator).build());
        }));
    }

    private Mono<Void> returnMono(GatewayFilterChain chain, ServerWebExchange exchange) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long startTime = exchange.getAttribute("startTime");
            if (startTime != null) {
                long executeTime = (System.currentTimeMillis() - startTime);
                log.info("接口:{} 状态码:{} 耗时:{}ms", exchange.getRequest().getURI().getRawPath(),
                        Objects.requireNonNull(exchange.getResponse().getStatusCode()).value(), executeTime);
            }

            int value = Objects.requireNonNull(exchange.getResponse().getStatusCode()).value();
            String rawPath = exchange.getRequest().getURI().getRawPath();
            Date d = new Date();
            SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmm");
            String format = sbf.format(d);
            String failKey = "count:" + "url" + ":" + "fail" + ":" + format + ":" + rawPath;
            String successKey = "count:" + "url" + ":" + "success" + ":" + format + ":" + rawPath;
            log.info("failKey:【{}】", failKey);
            log.info("successKey:【{}】", successKey);
            /* getBoolean(failKey);
            getBoolean(successKey);
            if (value == 200) {
                redisUtil.incrBy(successKey, 1);
                log.info("successKey-value值:【{}】", redisUtil.get(successKey));
            } else {
                redisUtil.incrBy(failKey, 1);
                log.info("failKey-value值:【{}】", redisUtil.get(failKey));
            }*/

        }));
    }

/*    private void getBoolean(String key) {
        if (!redisUtil.hasKey(key)) {
            redisUtil.set(key, "0");
            redisUtil.expire(key, 10, TimeUnit.MINUTES);
        }
    }*/


    @Override
    public int getOrder() {
        return -100;
    }


}
