# 日记系统实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现一个个人日记/清单系统，支持注册登录、清单管理、日记记录、图片上传，通过日历视图查看历史日记。

**Architecture:** 轻量级微服务架构，Gateway统一入口，User服务处理认证，Core服务处理业务逻辑。前端Vue 3 + Element Plus。

**Tech Stack:** Spring Boot 3.4, Spring Cloud Gateway, Spring 6.2, MyBatis-Plus 3.5, MySQL 8.0, JWT (jjwt 0.12.x), Vue 3.4, Vite 5.x, Element Plus 2.9.x, Pinia 2.x

---

## 阶段一：基础骨架

### Task 1: 创建多模块Maven项目脚手架

**Files:**
- Create: `diary-system/pom.xml` (父POM)
- Create: `diary-system/diary-common/pom.xml`
- Create: `diary-system/diary-common/src/main/java/com/diary/common/core/domain/R.java`
- Create: `diary-system/diary-common/src/main/java/com/diary/common/core/exception/GlobalExceptionHandler.java`
- Create: `diary-system/diary-common/src/main/resources/application-common.yml`

- [ ] **Step 1: 创建父POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.5</version>
        <relativePath/>
    </parent>

    <groupId>com.diary</groupId>
    <artifactId>diary-system</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>diary-system</name>
    <description>个人日记系统</description>

    <modules>
        <module>diary-common</module>
        <module>diary-gateway</module>
        <module>diary-user</module>
        <module>diary-core</module>
    </modules>

    <properties>
        <java.version>17</java.version>
        <spring-cloud.version>2023.0.4</spring-cloud.version>
        <mybatis-plus.version>3.5.9</mybatis-plus.version>
        <jjwt.version>0.12.6</jjwt.version>
        <hutool.version>5.8.32</hutool.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jjwt.version}</version>
            </dependency>
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>${hutool.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

- [ ] **Step 2: 创建diary-common模块POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.diary</groupId>
        <artifactId>diary-system</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>diary-common</artifactId>
    <name>diary-common</name>
    <description>公共模块</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

- [ ] **Step 3: 创建统一响应封装类**

```java
package com.diary.common.core.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.setCode(500);
        r.setMsg(msg);
        return r;
    }

    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }
}
```

- [ ] **Step 4: 创建全局异常处理**

```java
package com.diary.common.core.exception;

import com.diary.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public R<?> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return R.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统繁忙，请稍后重试");
    }
}
```

- [ ] **Step 5: 创建业务异常类**

```java
package com.diary.common.core.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
```

- [ ] **Step 6: Commit**

```bash
git add diary-system/
git commit -m "feat: 创建多模块Maven项目脚手架和公共模块"
```

### Task 2: 搭建Gateway网关

**Files:**
- Create: `diary-system/diary-gateway/pom.xml`
- Create: `diary-system/diary-gateway/src/main/java/com/diary/gateway/DiaryGatewayApplication.java`
- Create: `diary-system/diary-gateway/src/main/java/com/diary/gateway/config/GatewayConfig.java`
- Create: `diary-system/diary-gateway/src/main/java/com/diary/gateway/filter/JwtAuthFilter.java`
- Create: `diary-system/diary-gateway/src/main/resources/application.yml`

- [ ] **Step 1: 创建Gateway POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.diary</groupId>
        <artifactId>diary-system</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>diary-gateway</artifactId>
    <name>diary-gateway</name>
    <description>网关模块</description>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建Gateway启动类**

```java
package com.diary.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiaryGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiaryGatewayApplication.class, args);
    }
}
```

- [ ] **Step 3: 创建JWT认证过滤器**

```java
package com.diary.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret:diary-system-secret-key-must-be-at-least-256-bits-long!!}")
    private String secret;

    private static final List<String> WHITE_LIST = List.of(
            "/api/user/register",
            "/api/user/login/account",
            "/api/user/login/sms",
            "/api/user/sms/send"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        if (WHITE_LIST.contains(path)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange);
        }

        String token = authHeader.substring(7);
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject();
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return unauthorized(exchange);
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"code\":401,\"msg\":\"未登录或token已过期\"}";
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
```

- [ ] **Step 4: 创建Gateway配置**

```java
package com.diary.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GatewayConfig {

    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
```

- [ ] **Step 5: 创建Gateway配置文件**

```yaml
server:
  port: 8080

spring:
  application:
    name: diary-gateway
  cloud:
    gateway:
      routes:
        - id: diary-user
          uri: http://localhost:8081
          predicates:
            - Path=/api/user/**
        - id: diary-core
          uri: http://localhost:8082
          predicates:
            - Path=/api/diary/**

jwt:
  secret: diary-system-secret-key-must-be-at-least-256-bits-long!!
```

- [ ] **Step 6: Commit**

```bash
git add diary-system/diary-gateway/
git commit -m "feat: 搭建Gateway网关，实现路由转发和JWT鉴权"
```

### Task 3: 实现User服务基础框架

**Files:**
- Create: `diary-system/diary-user/pom.xml`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/DiaryUserApplication.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/entity/SysUser.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/mapper/SysUserMapper.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/service/SysUserService.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/service/impl/SysUserServiceImpl.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/controller/UserController.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/dto/RegisterDTO.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/dto/LoginDTO.java`
- Create: `diary-system/diary-user/src/main/java/com/diary/user/dto/LoginVO.java`
- Create: `diary-system/diary-user/src/main/resources/application.yml`
- Create: `diary-system/diary-user/src/main/resources/mapper/SysUserMapper.xml`

- [ ] **Step 1: 创建User服务POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.diary</groupId>
        <artifactId>diary-system</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>diary-user</artifactId>
    <name>diary-user</name>
    <description>用户模块</description>

    <dependencies>
        <dependency>
            <groupId>com.diary</groupId>
            <artifactId>diary-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建User实体类**

```java
package com.diary.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String phone;

    private String nickname;

    private String avatar;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

- [ ] **Step 3: 创建User Mapper**

```java
package com.diary.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.user.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
}
```

- [ ] **Step 4: 创建RegisterDTO**

```java
package com.diary.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度2-20个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度6-20个字符")
    private String password;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
}
```

- [ ] **Step 5: 创建LoginDTO**

```java
package com.diary.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
```

- [ ] **Step 6: 创建LoginVO**

```java
package com.diary.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVO {
    private String token;
    private String username;
    private String nickname;
}
```

- [ ] **Step 7: 创建SysUserService接口**

```java
package com.diary.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    void register(RegisterDTO dto);

    LoginVO login(LoginDTO dto);

    SysUser getUserById(Long id);
}
```

- [ ] **Step 8: 创建SysUserServiceImpl**

```java
package com.diary.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;
import com.diary.user.mapper.SysUserMapper;
import com.diary.user.service.SysUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${jwt.secret:diary-system-secret-key-must-be-at-least-256-bits-long!!}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @Override
    public void register(RegisterDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, dto.getUsername())
                .or()
                .eq(SysUser::getPhone, dto.getPhone());
        if (this.count(wrapper) > 0) {
            throw new BusinessException("用户名或手机号已存在");
        }

        SysUser user = new SysUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setNickname(dto.getUsername());
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        this.save(user);
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, dto.getUsername());
        SysUser user = this.getOne(wrapper);

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        String token = generateToken(user.getId());
        return LoginVO.builder()
                .token(token)
                .username(user.getUsername())
                .nickname(user.getNickname())
                .build();
    }

    @Override
    public SysUser getUserById(Long id) {
        return this.getById(id);
    }

    private String generateToken(Long userId) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }
}
```

- [ ] **Step 9: 创建UserController**

```java
package com.diary.user.controller;

import com.diary.common.core.domain.R;
import com.diary.user.dto.LoginDTO;
import com.diary.user.dto.LoginVO;
import com.diary.user.dto.RegisterDTO;
import com.diary.user.entity.SysUser;
import com.diary.user.service.SysUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final SysUserService sysUserService;

    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        sysUserService.register(dto);
        return R.ok();
    }

    @PostMapping("/login/account")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(sysUserService.login(dto));
    }

    @GetMapping("/info")
    public R<SysUser> getUserInfo(@RequestHeader("X-User-Id") Long userId) {
        return R.ok(sysUserService.getUserById(userId));
    }
}
```

- [ ] **Step 10: 创建User服务配置文件**

```yaml
server:
  port: 8081

spring:
  application:
    name: diary-user
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/diary?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

jwt:
  secret: diary-system-secret-key-must-be-at-least-256-bits-long!!
  expiration: 86400000

logging:
  level:
    com.diary: debug
```

- [ ] **Step 11: 创建数据库初始化SQL**

```sql
-- diary-system/sql/init.sql
CREATE DATABASE IF NOT EXISTS diary DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE diary;

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用 1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

- [ ] **Step 12: Commit**

```bash
git add diary-system/diary-user/ diary-system/sql/
git commit -m "feat: 实现User服务，支持注册和登录功能"
```

### Task 4: 验证阶段一成果

- [ ] **Step 1: 启动MySQL并创建数据库**

```bash
mysql -u root -p < diary-system/sql/init.sql
```

- [ ] **Step 2: 编译项目**

```bash
cd diary-system
mvn clean install -DskipTests
```

- [ ] **Step 3: 启动User服务**

```bash
cd diary-user
mvn spring-boot:run
```

- [ ] **Step 4: 测试注册接口**

```bash
curl -X POST http://localhost:8081/api/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","phone":"13800138000"}'
```

Expected: `{"code":200,"msg":"success","data":null}`

- [ ] **Step 5: 测试登录接口**

```bash
curl -X POST http://localhost:8081/api/user/login/account \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

Expected: `{"code":200,"msg":"success","data":{"token":"xxx","username":"test","nickname":"test"}}`

- [ ] **Step 6: 启动Gateway并测试统一入口**

```bash
cd diary-gateway
mvn spring-boot:run
```

```bash
curl -X POST http://localhost:8080/api/user/login/account \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

Expected: 返回token

- [ ] **Step 7: 测试鉴权拦截**

```bash
curl http://localhost:8080/api/user/info
```

Expected: `{"code":401,"msg":"未登录或token已过期"}`

- [ ] **Step 8: 测试带token访问**

```bash
TOKEN="上一步返回的token"
curl http://localhost:8080/api/user/info \
  -H "Authorization: Bearer $TOKEN"
```

Expected: 返回用户信息

- [ ] **Step 9: Commit**

```bash
git add .
git commit -m "test: 验证阶段一注册登录功能正常"
```

---

## 阶段二：核心功能

### Task 5: 创建Core服务基础框架

**Files:**
- Create: `diary-system/diary-core/pom.xml`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/DiaryCoreApplication.java`
- Create: `diary-system/diary-core/src/main/resources/application.yml`

- [ ] **Step 1: 创建Core服务POM**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.diary</groupId>
        <artifactId>diary-system</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>diary-core</artifactId>
    <name>diary-core</name>
    <description>日记核心模块</description>

    <dependencies>
        <dependency>
            <groupId>com.diary</groupId>
            <artifactId>diary-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: 创建Core启动类**

```java
package com.diary.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.diary.core.mapper")
public class DiaryCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(DiaryCoreApplication.class, args);
    }
}
```

- [ ] **Step 3: 创建Core配置文件**

```yaml
server:
  port: 8082

spring:
  application:
    name: diary-core
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/diary?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

diary:
  image:
    upload-path: ./uploads/images

logging:
  level:
    com.diary: debug
```

- [ ] **Step 4: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 创建Core服务基础框架"
```

### Task 6: 实现分类管理

**Files:**
- Create: `diary-system/diary-core/src/main/java/com/diary/core/entity/DiaryCategory.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/mapper/DiaryCategoryMapper.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/DiaryCategoryService.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/impl/DiaryCategoryServiceImpl.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/controller/CategoryController.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/dto/CategoryDTO.java`

- [ ] **Step 1: 创建分类实体类**

```java
package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("diary_category")
public class DiaryCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String name;

    private Integer sortOrder;

    private Integer isPreset;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
```

- [ ] **Step 2: 创建分类Mapper**

```java
package com.diary.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.core.entity.DiaryCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryCategoryMapper extends BaseMapper<DiaryCategory> {
}
```

- [ ] **Step 3: 创建CategoryDTO**

```java
package com.diary.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDTO {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称最长50个字符")
    private String name;

    private Integer sortOrder;
}
```

- [ ] **Step 4: 创建CategoryService接口**

```java
package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;

import java.util.List;

public interface DiaryCategoryService extends IService<DiaryCategory> {

    List<DiaryCategory> listByUserId(Long userId);

    void create(Long userId, CategoryDTO dto);

    void update(Long userId, Long id, CategoryDTO dto);

    void delete(Long userId, Long id);
}
```

- [ ] **Step 5: 创建CategoryServiceImpl**

```java
package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.mapper.DiaryCategoryMapper;
import com.diary.core.service.DiaryCategoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryCategoryServiceImpl extends ServiceImpl<DiaryCategoryMapper, DiaryCategory> implements DiaryCategoryService {

    @Override
    public List<DiaryCategory> listByUserId(Long userId) {
        LambdaQueryWrapper<DiaryCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DiaryCategory::getUserId, userId)
                .orderByAsc(DiaryCategory::getSortOrder);
        return this.list(wrapper);
    }

    @Override
    public void create(Long userId, CategoryDTO dto) {
        long count = this.count(new LambdaQueryWrapper<DiaryCategory>()
                .eq(DiaryCategory::getUserId, userId)
                .eq(DiaryCategory::getName, dto.getName()));
        if (count > 0) {
            throw new BusinessException("分类名称已存在");
        }

        DiaryCategory category = new DiaryCategory();
        category.setUserId(userId);
        category.setName(dto.getName());
        category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 99);
        category.setIsPreset(0);
        category.setCreateTime(LocalDateTime.now());
        this.save(category);
    }

    @Override
    public void update(Long userId, Long id, CategoryDTO dto) {
        DiaryCategory category = this.getById(id);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }
        if (category.getIsPreset() == 1) {
            throw new BusinessException("预设分类不可修改");
        }
        category.setName(dto.getName());
        if (dto.getSortOrder() != null) {
            category.setSortOrder(dto.getSortOrder());
        }
        this.updateById(category);
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryCategory category = this.getById(id);
        if (category == null || !category.getUserId().equals(userId)) {
            throw new BusinessException("分类不存在");
        }
        if (category.getIsPreset() == 1) {
            throw new BusinessException("预设分类不可删除");
        }
        this.removeById(id);
    }
}
```

- [ ] **Step 6: 创建CategoryController**

```java
package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.dto.CategoryDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.service.DiaryCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diary/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final DiaryCategoryService categoryService;

    @GetMapping
    public R<List<DiaryCategory>> list(@RequestHeader("X-User-Id") Long userId) {
        return R.ok(categoryService.listByUserId(userId));
    }

    @PostMapping
    public R<Void> create(@RequestHeader("X-User-Id") Long userId,
                          @Valid @RequestBody CategoryDTO dto) {
        categoryService.create(userId, dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id,
                          @Valid @RequestBody CategoryDTO dto) {
        categoryService.update(userId, id, dto);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        categoryService.delete(userId, id);
        return R.ok();
    }
}
```

- [ ] **Step 7: 创建分类表SQL**

```sql
-- 追加到 init.sql
CREATE TABLE diary_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    is_preset TINYINT DEFAULT 0 COMMENT '是否预设：0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清单分类表';
```

- [ ] **Step 8: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 实现分类管理CRUD"
```

### Task 7: 实现清单管理

**Files:**
- Create: `diary-system/diary-core/src/main/java/com/diary/core/entity/DiaryItem.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/mapper/DiaryItemMapper.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/DiaryItemService.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/impl/DiaryItemServiceImpl.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/controller/ItemController.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/dto/ItemDTO.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/vo/ItemVO.java`

- [ ] **Step 1: 创建清单实体类**

```java
package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary_item")
public class DiaryItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long categoryId;

    private LocalDate diaryDate;

    private String content;

    private Integer isDone;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

- [ ] **Step 2: 创建清单Mapper**

```java
package com.diary.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.core.entity.DiaryItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryItemMapper extends BaseMapper<DiaryItem> {
}
```

- [ ] **Step 3: 创建ItemDTO**

```java
package com.diary.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemDTO {

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @NotNull(message = "日记日期不能为空")
    private LocalDate diaryDate;

    @NotBlank(message = "内容不能为空")
    @Size(max = 500, message = "内容最长500个字符")
    private String content;

    private Integer sortOrder;
}
```

- [ ] **Step 4: 创建ItemVO**

```java
package com.diary.core.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ItemVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private LocalDate diaryDate;
    private String content;
    private Integer isDone;
    private Integer sortOrder;
}
```

- [ ] **Step 5: 创建ItemService接口**

```java
package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.dto.ItemDTO;
import com.diary.core.entity.DiaryItem;
import com.diary.core.vo.ItemVO;

import java.time.LocalDate;
import java.util.List;

public interface DiaryItemService extends IService<DiaryItem> {

    List<ItemVO> listByDate(Long userId, LocalDate date);

    void create(Long userId, ItemDTO dto);

    void update(Long userId, Long id, ItemDTO dto);

    void toggle(Long userId, Long id);

    void delete(Long userId, Long id);
}
```

- [ ] **Step 6: 创建ItemServiceImpl**

```java
package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.dto.ItemDTO;
import com.diary.core.entity.DiaryCategory;
import com.diary.core.entity.DiaryItem;
import com.diary.core.mapper.DiaryItemMapper;
import com.diary.core.service.DiaryCategoryService;
import com.diary.core.service.DiaryItemService;
import com.diary.core.vo.ItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryItemServiceImpl extends ServiceImpl<DiaryItemMapper, DiaryItem> implements DiaryItemService {

    private final DiaryCategoryService categoryService;

    @Override
    public List<ItemVO> listByDate(Long userId, LocalDate date) {
        List<DiaryItem> items = this.list(new LambdaQueryWrapper<DiaryItem>()
                .eq(DiaryItem::getUserId, userId)
                .eq(DiaryItem::getDiaryDate, date)
                .orderByAsc(DiaryItem::getSortOrder));

        List<DiaryCategory> categories = categoryService.listByUserId(userId);
        Map<Long, String> categoryMap = categories.stream()
                .collect(Collectors.toMap(DiaryCategory::getId, DiaryCategory::getName));

        return items.stream().map(item -> ItemVO.builder()
                .id(item.getId())
                .categoryId(item.getCategoryId())
                .categoryName(categoryMap.get(item.getCategoryId()))
                .diaryDate(item.getDiaryDate())
                .content(item.getContent())
                .isDone(item.getIsDone())
                .sortOrder(item.getSortOrder())
                .build()).collect(Collectors.toList());
    }

    @Override
    public void create(Long userId, ItemDTO dto) {
        DiaryItem item = new DiaryItem();
        item.setUserId(userId);
        item.setCategoryId(dto.getCategoryId());
        item.setDiaryDate(dto.getDiaryDate());
        item.setContent(dto.getContent());
        item.setIsDone(0);
        item.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 99);
        item.setCreateTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        this.save(item);
    }

    @Override
    public void update(Long userId, Long id, ItemDTO dto) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        item.setCategoryId(dto.getCategoryId());
        item.setContent(dto.getContent());
        if (dto.getSortOrder() != null) {
            item.setSortOrder(dto.getSortOrder());
        }
        item.setUpdateTime(LocalDateTime.now());
        this.updateById(item);
    }

    @Override
    public void toggle(Long userId, Long id) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        item.setIsDone(item.getIsDone() == 1 ? 0 : 1);
        item.setUpdateTime(LocalDateTime.now());
        this.updateById(item);
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryItem item = this.getById(id);
        if (item == null || !item.getUserId().equals(userId)) {
            throw new BusinessException("清单不存在");
        }
        this.removeById(id);
    }
}
```

- [ ] **Step 7: 创建ItemController**

```java
package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.dto.ItemDTO;
import com.diary.core.service.DiaryItemService;
import com.diary.core.vo.ItemVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diary/items")
@RequiredArgsConstructor
public class ItemController {

    private final DiaryItemService itemService;

    @GetMapping
    public R<List<ItemVO>> list(@RequestHeader("X-User-Id") Long userId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(itemService.listByDate(userId, date));
    }

    @PostMapping
    public R<Void> create(@RequestHeader("X-User-Id") Long userId,
                          @Valid @RequestBody ItemDTO dto) {
        itemService.create(userId, dto);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Void> update(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id,
                          @Valid @RequestBody ItemDTO dto) {
        itemService.update(userId, id, dto);
        return R.ok();
    }

    @PutMapping("/{id}/toggle")
    public R<Void> toggle(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        itemService.toggle(userId, id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        itemService.delete(userId, id);
        return R.ok();
    }
}
```

- [ ] **Step 8: 创建清单表SQL**

```sql
-- 追加到 init.sql
CREATE TABLE diary_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    category_id BIGINT NOT NULL COMMENT '所属分类',
    diary_date DATE NOT NULL COMMENT '日记日期',
    content VARCHAR(500) NOT NULL COMMENT '条目内容',
    is_done TINYINT DEFAULT 0 COMMENT '是否完成：0未完成 1已完成',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清单条目表';
```

- [ ] **Step 9: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 实现清单管理CRUD和完成状态切换"
```

### Task 8: 实现日记文字内容管理

**Files:**
- Create: `diary-system/diary-core/src/main/java/com/diary/core/entity/DiaryContent.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/mapper/DiaryContentMapper.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/DiaryContentService.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/impl/DiaryContentServiceImpl.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/controller/ContentController.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/dto/ContentDTO.java`

- [ ] **Step 1: 创建日记内容实体类**

```java
package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary_content")
public class DiaryContent {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate diaryDate;

    private String textContent;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

- [ ] **Step 2: 创建日记内容Mapper**

```java
package com.diary.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.core.entity.DiaryContent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryContentMapper extends BaseMapper<DiaryContent> {
}
```

- [ ] **Step 3: 创建ContentDTO**

```java
package com.diary.core.dto;

import lombok.Data;

@Data
public class ContentDTO {
    private String textContent;
}
```

- [ ] **Step 4: 创建ContentService接口**

```java
package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.entity.DiaryContent;

import java.time.LocalDate;

public interface DiaryContentService extends IService<DiaryContent> {

    DiaryContent getByDate(Long userId, LocalDate date);

    void save(Long userId, LocalDate date, String textContent);
}
```

- [ ] **Step 5: 创建ContentServiceImpl**

```java
package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.core.entity.DiaryContent;
import com.diary.core.mapper.DiaryContentMapper;
import com.diary.core.service.DiaryContentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DiaryContentServiceImpl extends ServiceImpl<DiaryContentMapper, DiaryContent> implements DiaryContentService {

    @Override
    public DiaryContent getByDate(Long userId, LocalDate date) {
        return this.getOne(new LambdaQueryWrapper<DiaryContent>()
                .eq(DiaryContent::getUserId, userId)
                .eq(DiaryContent::getDiaryDate, date));
    }

    @Override
    public void save(Long userId, LocalDate date, String textContent) {
        DiaryContent content = this.getByDate(userId, date);
        if (content == null) {
            content = new DiaryContent();
            content.setUserId(userId);
            content.setDiaryDate(date);
            content.setTextContent(textContent);
            content.setCreateTime(LocalDateTime.now());
            content.setUpdateTime(LocalDateTime.now());
            this.save(content);
        } else {
            content.setTextContent(textContent);
            content.setUpdateTime(LocalDateTime.now());
            this.updateById(content);
        }
    }
}
```

- [ ] **Step 6: 创建ContentController**

```java
package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.entity.DiaryContent;
import com.diary.core.service.DiaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/diary/content")
@RequiredArgsConstructor
public class ContentController {

    private final DiaryContentService contentService;

    @GetMapping
    public R<DiaryContent> get(@RequestHeader("X-User-Id") Long userId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(contentService.getByDate(userId, date));
    }

    @PutMapping
    public R<Void> save(@RequestHeader("X-User-Id") Long userId,
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        @RequestBody ContentDTO dto) {
        contentService.save(userId, date, dto.getTextContent());
        return R.ok();
    }
}
```

- [ ] **Step 7: 创建日记内容表SQL**

```sql
-- 追加到 init.sql
CREATE TABLE diary_content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    diary_date DATE NOT NULL COMMENT '日记日期',
    text_content TEXT COMMENT '文字内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记内容表';
```

- [ ] **Step 8: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 实现日记文字内容管理"
```

### Task 9: 实现图片管理

**Files:**
- Create: `diary-system/diary-core/src/main/java/com/diary/core/entity/DiaryImage.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/mapper/DiaryImageMapper.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/DiaryImageService.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/service/impl/DiaryImageServiceImpl.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/controller/ImageController.java`

- [ ] **Step 1: 创建图片实体类**

```java
package com.diary.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diary_image")
public class DiaryImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate diaryDate;

    private String imageUrl;

    private Integer sortOrder;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
```

- [ ] **Step 2: 创建图片Mapper**

```java
package com.diary.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.diary.core.entity.DiaryImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiaryImageMapper extends BaseMapper<DiaryImage> {
}
```

- [ ] **Step 3: 创建ImageService接口**

```java
package com.diary.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diary.core.entity.DiaryImage;

import java.time.LocalDate;
import java.util.List;

public interface DiaryImageService extends IService<DiaryImage> {

    List<DiaryImage> listByDate(Long userId, LocalDate date);

    DiaryImage upload(Long userId, LocalDate date, String imageUrl);

    void delete(Long userId, Long id);
}
```

- [ ] **Step 4: 创建ImageServiceImpl**

```java
package com.diary.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diary.common.core.exception.BusinessException;
import com.diary.core.entity.DiaryImage;
import com.diary.core.mapper.DiaryImageMapper;
import com.diary.core.service.DiaryImageService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiaryImageServiceImpl extends ServiceImpl<DiaryImageMapper, DiaryImage> implements DiaryImageService {

    @Override
    public List<DiaryImage> listByDate(Long userId, LocalDate date) {
        return this.list(new LambdaQueryWrapper<DiaryImage>()
                .eq(DiaryImage::getUserId, userId)
                .eq(DiaryImage::getDiaryDate, date)
                .orderByAsc(DiaryImage::getSortOrder));
    }

    @Override
    public DiaryImage upload(Long userId, LocalDate date, String imageUrl) {
        DiaryImage image = new DiaryImage();
        image.setUserId(userId);
        image.setDiaryDate(date);
        image.setImageUrl(imageUrl);
        image.setSortOrder(99);
        image.setCreateTime(LocalDateTime.now());
        this.save(image);
        return image;
    }

    @Override
    public void delete(Long userId, Long id) {
        DiaryImage image = this.getById(id);
        if (image == null || !image.getUserId().equals(userId)) {
            throw new BusinessException("图片不存在");
        }
        this.removeById(id);
    }
}
```

- [ ] **Step 5: 创建ImageController**

```java
package com.diary.core.controller;

import com.diary.common.core.domain.R;
import com.diary.core.entity.DiaryImage;
import com.diary.core.service.DiaryImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/diary/images")
@RequiredArgsConstructor
public class ImageController {

    private final DiaryImageService imageService;

    @Value("${diary.image.upload-path:./uploads/images}")
    private String uploadPath;

    @GetMapping
    public R<List<DiaryImage>> list(@RequestHeader("X-User-Id") Long userId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return R.ok(imageService.listByDate(userId, date));
    }

    @PostMapping("/upload")
    public R<DiaryImage> upload(@RequestHeader("X-User-Id") Long userId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                @RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;

        Path uploadDir = Paths.get(uploadPath);
        Files.createDirectories(uploadDir);
        Files.copy(file.getInputStream(), uploadDir.resolve(fileName));

        String imageUrl = "/uploads/images/" + fileName;
        DiaryImage image = imageService.upload(userId, date, imageUrl);
        return R.ok(image);
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@RequestHeader("X-User-Id") Long userId,
                          @PathVariable Long id) {
        imageService.delete(userId, id);
        return R.ok();
    }
}
```

- [ ] **Step 6: 创建图片表SQL**

```sql
-- 追加到 init.sql
CREATE TABLE diary_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    diary_date DATE NOT NULL COMMENT '所属日期',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    INDEX idx_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记图片表';
```

- [ ] **Step 7: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 实现图片上传和管理"
```

### Task 10: 实现日历查询

**Files:**
- Create: `diary-system/diary-core/src/main/java/com/diary/core/controller/CalendarController.java`
- Create: `diary-system/diary-core/src/main/java/com/diary/core/mapper/DiaryContentMapper.xml`

- [ ] **Step 1: 创建CalendarController**

```java
package com.diary.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.diary.common.core.domain.R;
import com.diary.core.entity.DiaryContent;
import com.diary.core.service.DiaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diary/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final DiaryContentService contentService;

    @GetMapping
    public R<List<LocalDate>> getDiaryDates(@RequestHeader("X-User-Id") Long userId,
                                            @RequestParam int year,
                                            @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<DiaryContent> contents = contentService.list(new LambdaQueryWrapper<DiaryContent>()
                .eq(DiaryContent::getUserId, userId)
                .between(DiaryContent::getDiaryDate, startDate, endDate)
                .select(DiaryContent::getDiaryDate));

        List<LocalDate> dates = contents.stream()
                .map(DiaryContent::getDiaryDate)
                .distinct()
                .collect(Collectors.toList());

        return R.ok(dates);
    }
}
```

- [ ] **Step 2: Commit**

```bash
git add diary-system/diary-core/
git commit -m "feat: 实现日历查询接口"
```

### Task 11: 验证阶段二成果

- [ ] **Step 1: 执行SQL初始化数据库**

```bash
mysql -u root -p < diary-system/sql/init.sql
```

- [ ] **Step 2: 编译项目**

```bash
cd diary-system
mvn clean install -DskipTests
```

- [ ] **Step 3: 启动所有服务**

```bash
# 终端1
cd diary-user && mvn spring-boot:run

# 终端2
cd diary-core && mvn spring-boot:run

# 终端3
cd diary-gateway && mvn spring-boot:run
```

- [ ] **Step 4: 获取token**

```bash
curl -X POST http://localhost:8080/api/user/login/account \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

- [ ] **Step 5: 测试分类列表**

```bash
TOKEN="上一步返回的token"
curl http://localhost:8080/api/diary/categories \
  -H "Authorization: Bearer $TOKEN"
```

Expected: 返回空列表（新用户无分类）

- [ ] **Step 6: 测试创建分类**

```bash
curl -X POST http://localhost:8080/api/diary/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name":"工作","sortOrder":1}'
```

Expected: `{"code":200,"msg":"success","data":null}`

- [ ] **Step 7: 测试创建清单**

```bash
curl -X POST http://localhost:8080/api/diary/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"categoryId":1,"diaryDate":"2026-05-22","content":"完成设计文档"}'
```

Expected: `{"code":200,"msg":"success","data":null}`

- [ ] **Step 8: 测试获取清单列表**

```bash
curl "http://localhost:8080/api/diary/items?date=2026-05-22" \
  -H "Authorization: Bearer $TOKEN"
```

Expected: 返回刚创建的清单

- [ ] **Step 9: 测试保存日记文字**

```bash
curl -X PUT "http://localhost:8080/api/diary/content?date=2026-05-22" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"textContent":"今天完成了系统设计"}'
```

Expected: `{"code":200,"msg":"success","data":null}`

- [ ] **Step 10: 测试日历查询**

```bash
curl "http://localhost:8080/api/diary/calendar?year=2026&month=5" \
  -H "Authorization: Bearer $TOKEN"
```

Expected: 返回包含2026-05-22的日期列表

- [ ] **Step 11: Commit**

```bash
git add .
git commit -m "test: 验证阶段二核心功能正常"
```

---

## 阶段三：前端实现

### Task 12: 创建Vue项目脚手架

**Files:**
- Create: `diary-vue/` (Vue项目)
- Create: `diary-vue/package.json`
- Create: `diary-vue/vite.config.js`
- Create: `diary-vue/src/main.js`
- Create: `diary-vue/src/App.vue`
- Create: `diary-vue/src/router/index.js`
- Create: `diary-vue/src/stores/user.js`
- Create: `diary-vue/src/utils/request.js`
- Create: `diary-vue/src/api/user.js`

- [ ] **Step 1: 创建Vue项目**

```bash
cd diary-system
npm create vue@latest diary-vue -- --template vue
cd diary-vue
npm install
npm install element-plus @element-plus/icons-vue pinia vue-router axios
```

- [ ] **Step 2: 配置Vite**

```javascript
// diary-vue/vite.config.js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

- [ ] **Step 3: 配置Element Plus**

```javascript
// diary-vue/src/main.js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
```

- [ ] **Step 4: 配置路由**

```javascript
// diary-vue/src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/diary',
    children: [
      {
        path: 'diary',
        name: 'DiaryEdit',
        component: () => import('../views/DiaryEdit.vue')
      },
      {
        path: 'calendar',
        name: 'Calendar',
        component: () => import('../views/Calendar.vue')
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/Category.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
```

- [ ] **Step 5: 配置请求工具**

```javascript
// diary-vue/src/utils/request.js
import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.msg || '请求失败')
      if (res.code === 401) {
        localStorage.removeItem('token')
        router.push('/login')
      }
      return Promise.reject(new Error(res.msg))
    }
    return res
  },
  error => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
```

- [ ] **Step 6: 配置用户API**

```javascript
// diary-vue/src/api/user.js
import request from '../utils/request'

export function register(data) {
  return request.post('/user/register', data)
}

export function login(data) {
  return request.post('/user/login/account', data)
}

export function getUserInfo() {
  return request.get('/user/info')
}
```

- [ ] **Step 7: 配置Pinia Store**

```javascript
// diary-vue/src/stores/user.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getUserInfo } from '../api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('token')
  }

  async function fetchUserInfo() {
    const res = await getUserInfo()
    userInfo.value = res.data
  }

  return { token, userInfo, setToken, logout, fetchUserInfo }
})
```

- [ ] **Step 8: Commit**

```bash
git add diary-vue/
git commit -m "feat: 创建Vue项目脚手架，配置路由和请求工具"
```

### Task 13: 实现登录注册页

**Files:**
- Create: `diary-vue/src/views/Login.vue`

- [ ] **Step 1: 创建登录注册页**

```vue
<!-- diary-vue/src/views/Login.vue -->
<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>日记系统</h2>
      <el-tabs v-model="activeTab">
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="loginForm.password" type="password" placeholder="密码" prefix-icon="Lock" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleLogin" style="width:100%">
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
            <el-form-item prop="username">
              <el-input v-model="registerForm.username" placeholder="用户名" prefix-icon="User" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input v-model="registerForm.password" type="password" placeholder="密码" prefix-icon="Lock" />
            </el-form-item>
            <el-form-item prop="phone">
              <el-input v-model="registerForm.phone" placeholder="手机号" prefix-icon="Phone" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleRegister" style="width:100%">
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/user'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('login')
const loading = ref(false)
const loginFormRef = ref()
const registerFormRef = ref()

const loginForm = reactive({ username: '', password: '' })
const registerForm = reactive({ username: '', password: '', phone: '' })

const loginRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度2-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度6-20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ]
}

async function handleLogin() {
  await loginFormRef.value.validate()
  loading.value = true
  try {
    const res = await login(loginForm)
    userStore.setToken(res.data.token)
    ElMessage.success('登录成功')
    router.push('/')
  } finally {
    loading.value = false
  }
}

async function handleRegister() {
  await registerFormRef.value.validate()
  loading.value = true
  try {
    await register(registerForm)
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login'
    loginForm.username = registerForm.username
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 400px;
}
h2 {
  text-align: center;
  margin-bottom: 20px;
}
</style>
```

- [ ] **Step 2: Commit**

```bash
git add diary-vue/src/views/Login.vue
git commit -m "feat: 实现登录注册页"
```

### Task 14: 实现主布局和日记编辑页

**Files:**
- Create: `diary-vue/src/views/Layout.vue`
- Create: `diary-vue/src/views/DiaryEdit.vue`
- Create: `diary-vue/src/api/diary.js`

- [ ] **Step 1: 创建日记API**

```javascript
// diary-vue/src/api/diary.js
import request from '../utils/request'

export function getCategories() {
  return request.get('/diary/categories')
}

export function createCategory(data) {
  return request.post('/diary/categories', data)
}

export function updateCategory(id, data) {
  return request.put(`/diary/categories/${id}`, data)
}

export function deleteCategory(id) {
  return request.delete(`/diary/categories/${id}`)
}

export function getItems(date) {
  return request.get('/diary/items', { params: { date } })
}

export function createItem(data) {
  return request.post('/diary/items', data)
}

export function updateItem(id, data) {
  return request.put(`/diary/items/${id}`, data)
}

export function toggleItem(id) {
  return request.put(`/diary/items/${id}/toggle`)
}

export function deleteItem(id) {
  return request.delete(`/diary/items/${id}`)
}

export function getContent(date) {
  return request.get('/diary/content', { params: { date } })
}

export function saveContent(date, data) {
  return request.put(`/diary/content?date=${date}`, data)
}

export function getImages(date) {
  return request.get('/diary/images', { params: { date } })
}

export function uploadImage(date, file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post(`/diary/images/upload?date=${date}`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

export function deleteImage(id) {
  return request.delete(`/diary/images/${id}`)
}

export function getCalendarDates(year, month) {
  return request.get('/diary/calendar', { params: { year, month } })
}
```

- [ ] **Step 2: 创建主布局页**

```vue
<!-- diary-vue/src/views/Layout.vue -->
<template>
  <el-container class="layout-container">
    <el-header>
      <div class="header-left">
        <h1>日记系统</h1>
      </div>
      <div class="header-right">
        <span>{{ userStore.userInfo?.nickname }}</span>
        <el-button text @click="handleLogout">退出</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu :default-active="route.path" router>
          <el-menu-item index="/diary">
            <el-icon><Edit /></el-icon>
            <span>今日日记</span>
          </el-menu-item>
          <el-menu-item index="/calendar">
            <el-icon><Calendar /></el-icon>
            <span>日历查看</span>
          </el-menu-item>
          <el-menu-item index="/category">
            <el-icon><Setting /></el-icon>
            <span>分类管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
  try {
    await userStore.fetchUserInfo()
  } catch {
    userStore.logout()
    router.push('/login')
  }
})

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 10px;
}
.el-aside {
  border-right: 1px solid #eee;
}
</style>
```

- [ ] **Step 3: 创建日记编辑页**

```vue
<!-- diary-vue/src/views/DiaryEdit.vue -->
<template>
  <div class="diary-edit">
    <div class="date-header">
      <el-button @click="changeDate(-1)">
        <el-icon><ArrowLeft /></el-icon>
      </el-button>
      <el-date-picker v-model="currentDate" type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
        @change="loadData" />
      <el-button @click="changeDate(1)">
        <el-icon><ArrowRight /></el-icon>
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="section-card">
          <template #header>
            <div class="card-header">
              <span>清单</span>
              <el-button type="primary" size="small" @click="showAddItem">新增</el-button>
            </div>
          </template>
          <div v-for="category in categories" :key="category.id" class="category-group">
            <h4>{{ category.name }}</h4>
            <div v-for="item in getItemsByCategory(category.id)" :key="item.id" class="item-row">
              <el-checkbox v-model="item.isDone" :true-value="1" :false-value="0"
                @change="handleToggle(item)">
                <span :class="{ done: item.isDone === 1 }">{{ item.content }}</span>
              </el-checkbox>
              <el-button text type="danger" size="small" @click="handleDeleteItem(item.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          <el-empty v-if="items.length === 0" description="暂无清单" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="section-card">
          <template #header>
            <span>日记</span>
          </template>
          <el-input v-model="textContent" type="textarea" :rows="10" placeholder="记录今天的想法..."
            @blur="handleSaveContent" />
        </el-card>
        <el-card class="section-card" style="margin-top: 20px;">
          <template #header>
            <div class="card-header">
              <span>图片</span>
              <el-upload :show-file-list="false" :before-upload="handleUpload">
                <el-button type="primary" size="small">上传</el-button>
              </el-upload>
            </div>
          </template>
          <div class="image-list">
            <div v-for="img in images" :key="img.id" class="image-item">
              <el-image :src="img.imageUrl" fit="cover" :preview-src-list="[img.imageUrl]" />
              <el-button class="delete-btn" text type="danger" size="small"
                @click="handleDeleteImage(img.id)">
                <el-icon><Delete /></el-icon>
              </el-button>
            </div>
          </div>
          <el-empty v-if="images.length === 0" description="暂无图片" />
        </el-card>
      </el-col>
    </el-row>

    <el-dialog v-model="itemDialogVisible" title="新增清单" width="400px">
      <el-form :model="itemForm">
        <el-form-item label="分类">
          <el-select v-model="itemForm.categoryId" placeholder="选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="itemForm.content" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddItem">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'
import { getCategories, getItems, createItem, toggleItem, deleteItem,
  getContent, saveContent, getImages, uploadImage, deleteImage } from '../api/diary'

const currentDate = ref(dayjs().format('YYYY-MM-DD'))
const categories = ref([])
const items = ref([])
const textContent = ref('')
const images = ref([])
const itemDialogVisible = ref(false)
const itemForm = reactive({ categoryId: null, content: '' })

function getItemsByCategory(categoryId) {
  return items.value.filter(item => item.categoryId === categoryId)
}

function changeDate(delta) {
  currentDate.value = dayjs(currentDate.value).add(delta, 'day').format('YYYY-MM-DD')
}

async function loadData() {
  const [catRes, itemRes, contentRes, imgRes] = await Promise.all([
    getCategories(),
    getItems(currentDate.value),
    getContent(currentDate.value),
    getImages(currentDate.value)
  ])
  categories.value = catRes.data
  items.value = itemRes.data
  textContent.value = contentRes.data?.textContent || ''
  images.value = imgRes.data
}

function showAddItem() {
  itemForm.categoryId = categories.value[0]?.id
  itemForm.content = ''
  itemDialogVisible.value = true
}

async function handleAddItem() {
  if (!itemForm.categoryId || !itemForm.content) {
    ElMessage.warning('请选择分类并输入内容')
    return
  }
  await createItem({
    categoryId: itemForm.categoryId,
    diaryDate: currentDate.value,
    content: itemForm.content
  })
  itemDialogVisible.value = false
  ElMessage.success('添加成功')
  loadData()
}

async function handleToggle(item) {
  await toggleItem(item.id)
}

async function handleDeleteItem(id) {
  await ElMessageBox.confirm('确定删除该清单？', '提示', { type: 'warning' })
  await deleteItem(id)
  ElMessage.success('删除成功')
  loadData()
}

let saveTimer = null
function handleSaveContent() {
  clearTimeout(saveTimer)
  saveTimer = setTimeout(async () => {
    await saveContent(currentDate.value, { textContent: textContent.value })
  }, 1000)
}

async function handleUpload(file) {
  await uploadImage(currentDate.value, file)
  ElMessage.success('上传成功')
  loadData()
  return false
}

async function handleDeleteImage(id) {
  await ElMessageBox.confirm('确定删除该图片？', '提示', { type: 'warning' })
  await deleteImage(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.diary-edit {
  padding: 20px;
}
.date-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 20px;
}
.section-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.category-group {
  margin-bottom: 15px;
}
.category-group h4 {
  margin-bottom: 10px;
  color: #666;
}
.item-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 5px 0;
}
.done {
  text-decoration: line-through;
  color: #999;
}
.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.image-item {
  position: relative;
  width: 100px;
  height: 100px;
}
.image-item .el-image {
  width: 100%;
  height: 100%;
}
.delete-btn {
  position: absolute;
  top: 0;
  right: 0;
  background: rgba(0,0,0,0.5);
  color: white;
}
</style>
```

- [ ] **Step 4: Commit**

```bash
git add diary-vue/src/
git commit -m "feat: 实现主布局和日记编辑页"
```

### Task 15: 实现日历查看页和分类管理页

**Files:**
- Create: `diary-vue/src/views/Calendar.vue`
- Create: `diary-vue/src/views/Category.vue`

- [ ] **Step 1: 创建日历查看页**

```vue
<!-- diary-vue/src/views/Calendar.vue -->
<template>
  <div class="calendar-page">
    <el-calendar v-model="currentDate">
      <template #date-cell="{ data }">
        <div class="calendar-cell" :class="{ 'has-diary': isDiaryDate(data.day) }"
          @click="handleDateClick(data.day)">
          {{ data.day.split('-')[2] }}
          <div v-if="isDiaryDate(data.day)" class="diary-dot"></div>
        </div>
      </template>
    </el-calendar>

    <el-dialog v-model="dialogVisible" :title="selectedDate + ' 日记'" width="60%">
      <div v-if="selectedDiary">
        <h3>清单</h3>
        <div v-for="category in selectedCategories" :key="category.id" class="category-group">
          <h4>{{ category.name }}</h4>
          <div v-for="item in getItemsByCategory(category.id)" :key="item.id" class="item-row">
            <el-checkbox :model-value="item.isDone === 1" disabled>
              <span :class="{ done: item.isDone === 1 }">{{ item.content }}</span>
            </el-checkbox>
          </div>
        </div>
        <el-empty v-if="selectedItems.length === 0" description="无清单" />

        <h3 style="margin-top: 20px;">日记</h3>
        <p>{{ selectedDiary.textContent || '无内容' }}</p>

        <h3 style="margin-top: 20px;">图片</h3>
        <div class="image-list">
          <el-image v-for="img in selectedImages" :key="img.id" :src="img.imageUrl"
            fit="cover" :preview-src-list="selectedImages.map(i => i.imageUrl)" />
        </div>
        <el-empty v-if="selectedImages.length === 0" description="无图片" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import dayjs from 'dayjs'
import { getCalendarDates, getCategories, getItems, getContent, getImages } from '../api/diary'

const currentDate = ref(new Date())
const diaryDates = ref([])
const dialogVisible = ref(false)
const selectedDate = ref('')
const selectedDiary = ref(null)
const selectedItems = ref([])
const selectedCategories = ref([])
const selectedImages = ref([])

function isDiaryDate(date) {
  return diaryDates.value.includes(date)
}

function getItemsByCategory(categoryId) {
  return selectedItems.value.filter(item => item.categoryId === categoryId)
}

async function loadCalendarDates() {
  const year = dayjs(currentDate.value).year()
  const month = dayjs(currentDate.value).month() + 1
  const res = await getCalendarDates(year, month)
  diaryDates.value = res.data.map(d => d)
}

async function handleDateClick(date) {
  selectedDate.value = date
  const [catRes, itemRes, contentRes, imgRes] = await Promise.all([
    getCategories(),
    getItems(date),
    getContent(date),
    getImages(date)
  ])
  selectedCategories.value = catRes.data
  selectedItems.value = itemRes.data
  selectedDiary.value = contentRes.data
  selectedImages.value = imgRes.data
  dialogVisible.value = true
}

watch(currentDate, loadCalendarDates)
onMounted(loadCalendarDates)
</script>

<style scoped>
.calendar-page {
  padding: 20px;
}
.calendar-cell {
  height: 100%;
  cursor: pointer;
}
.has-diary {
  font-weight: bold;
}
.diary-dot {
  width: 6px;
  height: 6px;
  background: #409eff;
  border-radius: 50%;
  margin: 2px auto 0;
}
.category-group {
  margin-bottom: 10px;
}
.category-group h4 {
  color: #666;
  margin-bottom: 5px;
}
.item-row {
  padding: 3px 0;
}
.done {
  text-decoration: line-through;
  color: #999;
}
.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.image-list .el-image {
  width: 100px;
  height: 100px;
}
</style>
```

- [ ] **Step 2: 创建分类管理页**

```vue
<!-- diary-vue/src/views/Category.vue -->
<template>
  <div class="category-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="showAdd">新增分类</el-button>
        </div>
      </template>
      <el-table :data="categories" stripe>
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="sortOrder" label="排序" width="100" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isPreset === 1 ? 'success' : 'info'">
              {{ row.isPreset === 1 ? '预设' : '自定义' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button v-if="row.isPreset !== 1" text type="primary" @click="showEdit(row)">
              编辑
            </el-button>
            <el-button v-if="row.isPreset !== 1" text type="danger" @click="handleDelete(row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="400px">
      <el-form :model="form">
        <el-form-item label="名称">
          <el-input v-model="form.name" placeholder="输入分类名称" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="1" :max="99" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCategories, createCategory, updateCategory, deleteCategory } from '../api/diary'

const categories = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = reactive({ name: '', sortOrder: 1 })

async function loadData() {
  const res = await getCategories()
  categories.value = res.data
}

function showAdd() {
  isEdit.value = false
  form.name = ''
  form.sortOrder = categories.value.length + 1
  dialogVisible.value = true
}

function showEdit(row) {
  isEdit.value = true
  editId.value = row.id
  form.name = row.name
  form.sortOrder = row.sortOrder
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!form.name) {
    ElMessage.warning('请输入分类名称')
    return
  }
  if (isEdit.value) {
    await updateCategory(editId.value, { name: form.name, sortOrder: form.sortOrder })
  } else {
    await createCategory({ name: form.name, sortOrder: form.sortOrder })
  }
  dialogVisible.value = false
  ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
  loadData()
}

async function handleDelete(id) {
  await ElMessageBox.confirm('确定删除该分类？', '提示', { type: 'warning' })
  await deleteCategory(id)
  ElMessage.success('删除成功')
  loadData()
}

onMounted(loadData)
</script>

<style scoped>
.category-page {
  padding: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
```

- [ ] **Step 3: Commit**

```bash
git add diary-vue/src/views/
git commit -m "feat: 实现日历查看页和分类管理页"
```

### Task 16: 验证阶段三成果

- [ ] **Step 1: 启动前端开发服务器**

```bash
cd diary-vue
npm run dev
```

- [ ] **Step 2: 浏览器访问**

打开 http://localhost:3000

- [ ] **Step 3: 测试注册**

- 输入用户名、密码、手机号
- 点击注册
- 预期：提示注册成功，自动切换到登录

- [ ] **Step 4: 测试登录**

- 输入用户名、密码
- 点击登录
- 预期：跳转到日记编辑页

- [ ] **Step 5: 测试日记编辑**

- 点击新增清单，选择分类，输入内容
- 勾选完成状态
- 输入日记文字
- 上传图片
- 预期：所有操作正常

- [ ] **Step 6: 测试日历查看**

- 点击左侧菜单"日历查看"
- 点击有日记的日期
- 预期：弹窗显示当天日记内容

- [ ] **Step 7: 测试分类管理**

- 点击左侧菜单"分类管理"
- 新增自定义分类
- 编辑分类
- 删除分类
- 预期：所有操作正常

- [ ] **Step 8: Commit**

```bash
git add .
git commit -m "test: 验证阶段三前端功能正常"
```

---

## 完成

实施计划完成。三个阶段全部实现后，系统具备：
1. 用户注册登录（JWT鉴权）
2. 清单管理（分类、增删改查、完成状态切换）
3. 日记文字记录（自动保存）
4. 图片上传和管理
5. 日历视图查看历史日记
6. 分类管理（预设+自定义）
