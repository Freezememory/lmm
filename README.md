# lmm Spring Cloud快速开发脚手架 #

## 核心依赖 ##

* ***Spring Cloud Alibaba 2021.1***
* ***Spring Boot   2.5.6***
* ***sa-Token 1.30.0***
* ***Mybatis-Plus 3.4.2***
* ***Hutool   5.0.5***
* ***Nacos 2.0.3***


## 模块说明 ##
````
.
│
│─lmm-auth                             --鉴权模块
│  
├─lmm-biz                              --应用模块
│   │
│   ├─lmm-admin                        --系统、用户模块   主要负责系统管理、用户管理等相关业务
│   │                                               
│   ├─lmm-admin-api                    --数据迁移同步模块
│   │                                                
│   ├─lmm-portal                       --第三方接口模块   
│                                                 
│
├─lmm-common                           --公共模块
│   │
│   ├─lmm-common-base                  --基础模块
│   │
│   ├─lmm-common-mybatis               --mybatis配置模块                                             
│   │
│   ├─lmm-common-redis                 --redis模块
│   │
│   ├─lmm-common-satoken               --sa-Token整合配置模块
│   │
│   ├─lmm-common-security              --安全校验模块 
│                                                   
│
├─lmm-gateway                          --网关模块   服务分发
│
│─lmm-protocol                         --远程接口调用模块
│
│─lmm-visual                           --系统监控模块
│
├─pom.xml                              --主要依赖
│                                  
├─README.md                            --项目开发说明

````

## 主要功能 ##

### 1、feign远程调用鉴权 ###

### 2、接口参数加解密 ###

### 3、整合sa-Token鉴权模块 ###

### 4、微服务快速部署 ###