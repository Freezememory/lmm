# 日记系统设计文档

## 1. 项目概述

一个个人日记/清单系统，记录用户每日所做所想。支持工作清单、生活清单及完成情况标记，提供文字记录和图片上传功能。以日历视图查看历史日记，页面简洁易用。

## 2. 技术栈

- **后端**：Spring Cloud Gateway + Spring Boot 3.4 + Spring 6.2
- **前端**：Vue 3.4 + Vite 5.x + Element Plus 2.9.x + Pinia 2.x + Vue Router 4.x
- **数据库**：MySQL 8.0
- **认证**：JWT Token（jjwt 0.12.x）
- **ORM**：MyBatis-Plus 3.5.x
- **图片存储**：本地文件系统（MVP），后续可迁移到OSS

## 3. 架构设计

### 3.1 轻量级微服务架构

Gateway做统一入口，用户服务和日记核心服务独立部署，代码按模块清晰划分。不引入注册中心（Eureka/Nacos），Gateway直接通过端口路由到各服务，保持架构简洁。后续如需服务治理，可平滑接入注册中心。

```
diary-system/
├── diary-gateway/              # Spring Cloud Gateway 网关
│   ├── 路由转发
│   ├── 统一鉴权（JWT校验）
│   └── 跨域处理
│
├── diary-common/               # 公共模块
│   ├── 通用工具类
│   ├── 统一响应封装
│   ├── 统一异常处理
│   └── 基础实体类
│
├── diary-user/                 # 用户模块
│   ├── 注册/登录（账号密码 + 手机验证码）
│   ├── JWT Token 管理
│   └── 用户信息管理
│
├── diary-core/                 # 日记核心模块
│   ├── 清单管理（CRUD、分类、完成标记）
│   ├── 日记管理（文字内容）
│   ├── 图片管理（上传、存储）
│   └── 日历查询
│
└── diary-vue/                  # Vue前端
    ├── 登录/注册页
    ├── 日记编辑页（当日）
    ├── 日历查看页（历史）
    └── 清单分类管理页
```

### 3.2 模块依赖关系

- `diary-gateway` → 依赖 `diary-user`、`diary-core`
- `diary-core` → 依赖 `diary-common`、`diary-user`（获取当前用户）
- `diary-user` → 依赖 `diary-common`
- 所有模块 → 依赖 `diary-common`

### 3.3 服务端口规划

- Gateway: 8080（对外统一入口）
- User服务: 8081
- Core服务: 8082

## 4. 数据库设计

### 4.1 用户表 `sys_user`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键，自增 |
| username | varchar(50) | 用户名，唯一 |
| password | varchar(100) | 密码（BCrypt加密） |
| phone | varchar(20) | 手机号，唯一 |
| nickname | varchar(50) | 昵称 |
| avatar | varchar(255) | 头像URL |
| status | tinyint | 状态：0禁用 1启用 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

### 4.2 清单分类表 `diary_category`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| name | varchar(50) | 分类名称（工作/生活/自定义） |
| sort_order | int | 排序序号 |
| is_preset | tinyint | 是否预设：0否 1是 |
| create_time | datetime | 创建时间 |

### 4.3 清单条目表 `diary_item`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| category_id | bigint | 所属分类 |
| diary_date | date | 日记日期 |
| content | varchar(500) | 条目内容 |
| is_done | tinyint | 是否完成：0未完成 1已完成 |
| sort_order | int | 排序序号 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

### 4.4 日记内容表 `diary_content`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| diary_date | date | 日记日期，与user_id联合唯一 |
| text_content | text | 文字内容 |
| create_time | datetime | 创建时间 |
| update_time | datetime | 更新时间 |

### 4.5 日记图片表 `diary_image`

| 字段 | 类型 | 说明 |
|------|------|------|
| id | bigint | 主键 |
| user_id | bigint | 所属用户 |
| diary_date | date | 所属日期 |
| image_url | varchar(500) | 图片URL |
| sort_order | int | 排序序号 |
| create_time | datetime | 上传时间 |

### 4.6 设计要点

- 以 `diary_date` 为维度聚合，一天的日记 = 多个清单条目 + 一段文字 + 多张图片
- 清单分类支持预设（系统初始化时为每个用户创建"工作""生活"）+ 用户自定义
- 图片存储方案：本地文件系统（MVP），后续可迁移到OSS

## 5. API设计

### 5.1 用户模块 `/api/user`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/register` | 用户注册（用户名+密码+手机号） |
| POST | `/login/account` | 账号密码登录 |
| POST | `/login/sms` | 手机验证码登录 |
| POST | `/sms/send` | 发送短信验证码 |
| GET | `/info` | 获取当前用户信息 |
| PUT | `/info` | 更新用户信息（昵称、头像） |

### 5.2 日记模块 `/api/diary`

**分类管理**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/categories` | 获取我的分类列表 |
| POST | `/categories` | 新增自定义分类 |
| PUT | `/categories/{id}` | 修改分类 |
| DELETE | `/categories/{id}` | 删除分类（预设不可删） |

**清单管理**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/items?date=2026-05-20` | 获取指定日期所有清单 |
| POST | `/items` | 新增清单条目 |
| PUT | `/items/{id}` | 修改条目内容 |
| PUT | `/items/{id}/toggle` | 切换完成状态 |
| DELETE | `/items/{id}` | 删除条目 |

**日记文字**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/content?date=2026-05-20` | 获取指定日期文字内容 |
| PUT | `/content?date=2026-05-20` | 保存/更新文字内容 |

**图片管理**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/images?date=2026-05-20` | 获取指定日期图片列表 |
| POST | `/images/upload` | 上传图片 |
| DELETE | `/images/{id}` | 删除图片 |

**日历查询**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/calendar?year=2026&month=5` | 获取某月有日记的日期列表 |

### 5.3 鉴权方式

JWT Token，登录成功后返回token，前端每次请求在Header中携带 `Authorization: Bearer {token}`，Gateway统一校验。

## 6. 前端页面设计

### 6.1 页面结构

```
diary-vue/
├── 登录/注册页（Login）
├── 主布局（Layout）
│   ├── 日记编辑页（DiaryEdit）—— 默认首页
│   ├── 日历查看页（Calendar）
│   └── 分类管理页（CategoryManage）
```

### 6.2 各页面核心功能

**登录/注册页**
- 登录表单：用户名/手机号 + 密码，或手机号 + 验证码
- 注册表单：用户名 + 密码 + 手机号
- Tab切换登录/注册

**日记编辑页（首页）**
- 顶部：日期选择器（默认今天），可左右翻页
- 清单区域：按分类分组显示，每条可打勾标记完成，可新增/编辑/删除条目
- 文字区域：富文本编辑器（简单即可），自动保存
- 图片区域：上传图片、预览、删除

**日历查看页**
- 日历控件，有日记的日期标点显示
- 点击日期 → 展示当天日记（只读模式）
- 支持切换月份

**分类管理页**
- 列表展示所有分类（预设+自定义）
- 可新增、编辑、删除自定义分类
- 拖拽排序

### 6.3 技术选型

- Vue 3 + Vite
- Element Plus（UI组件库）
- Vue Router + Pinia（状态管理）
- Axios（HTTP请求）

## 7. 核心业务流程

### 7.1 用户注册登录

```
用户 → 注册页面 → 填写用户名/密码/手机号
    → 后端校验 → 密码BCrypt加密 → 存入sys_user
    → 自动创建预设分类（工作、生活）
    → 返回注册成功 → 跳转登录

用户 → 登录页面 → 选择登录方式
    → 账号密码：校验用户名+密码 → 生成JWT Token
    → 手机验证码：发送短信 → 校验手机号+验证码 → 生成JWT Token
    → 前端存储Token → 进入日记首页
```

### 7.2 每日日记编辑

```
用户进入日记编辑页（默认今天）
    ├── 页面加载时并行请求：
    │   ├── GET /categories → 获取分类列表
    │   ├── GET /items?date=today → 获取今日清单
    │   ├── GET /content?date=today → 获取今日文字
    │   └── GET /images?date=today → 获取今日图片
    │
    ├── 清单操作：
    │   ├── 新增条目：选择分类 → 输入内容 → POST /items
    │   ├── 标记完成：点击勾选 → PUT /items/{id}/toggle
    │   ├── 编辑条目：修改内容 → PUT /items/{id}
    │   └── 删除条目：确认删除 → DELETE /items/{id}
    │
    ├── 文字操作：
    │   └── 编辑后失焦或点击保存 → PUT /content
    │
    └── 图片操作：
        ├── 上传：选择图片 → POST /images/upload → 返回URL → 前端预览
        └── 删除：确认 → DELETE /images/{id}
```

### 7.3 历史日记查看

```
用户进入日历页面
    → GET /calendar?year=2026&month=5 → 返回该月有日记的日期列表
    → 日历上有日记的日期显示圆点标记
    → 用户点击某个日期
        → 并行请求该日期的清单/文字/图片
        → 以只读模式展示当天日记
```

### 7.4 图片上传处理

```
用户选择图片 → 前端校验（类型jpg/png，大小限制5MB）
    → POST /images/upload（multipart/form-data）
    → 后端接收 → 生成唯一文件名 → 存储到本地目录
    → 记录图片URL到diary_image表
    → 返回图片访问URL
    → 前端展示预览
```

### 7.5 新用户初始化

```
用户注册成功
    → 自动创建预设分类：
    │   - "工作"（is_preset=1, sort_order=1）
    │   - "生活"（is_preset=1, sort_order=2）
    → 用户可在此基础上新增自定义分类
```

## 8. 实施阶段

采用分阶段递进策略，每阶段产出可独立运行验证：

| 阶段 | 内容 | 产出 |
|------|------|------|
| 一 | 项目脚手架 + Gateway + User服务 + 注册登录 | 可运行的登录注册系统 |
| 二 | Core服务 + 清单/日记/图片CRUD | 完整后端API |
| 三 | Vue前端完整实现 | 可使用的日记系统 |

### 阶段一：基础骨架
- 创建多模块Maven项目脚手架
- 搭建Gateway统一鉴权
- 实现User服务（注册/登录/JWT）
- 验证：可通过Postman完成注册登录流程

### 阶段二：核心功能
- 实现Core服务（清单/日记/图片CRUD）
- 对接MySQL数据库
- 验证：可通过Postman完成日记增删改查

### 阶段三：前端交互
- 实现Vue前端（登录/日记编辑/日历/分类管理）
- 对接后端API
- 验证：浏览器可完成完整日记流程

## 9. 非功能性设计

- **自动保存**：文字内容编辑后延迟2秒自动保存（防抖）
- **乐观更新**：清单勾选即时反馈，异步请求后端
- **图片压缩**：前端上传前压缩，减少传输时间
- **Token刷新**：Token过期前自动续期，避免用户频繁重新登录

## 10. 未来扩展预留

- **多端接入**：Gateway统一入口，前端可替换为微信小程序、移动端App等
- **服务治理**：接入Nacos/Eureka注册中心，实现服务发现与配置管理
- **图片存储迁移**：从本地文件系统迁移到OSS，只需修改图片服务实现
- **统计功能**：基于现有数据可扩展完成率、打卡天数等统计
