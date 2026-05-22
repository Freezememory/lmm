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

CREATE TABLE diary_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    is_preset TINYINT DEFAULT 0 COMMENT '是否预设：0否 1是',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='清单分类表';

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

CREATE TABLE diary_content (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    diary_date DATE NOT NULL COMMENT '日记日期',
    text_content TEXT COMMENT '文字内容',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记内容表';

CREATE TABLE diary_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '所属用户',
    diary_date DATE NOT NULL COMMENT '所属日期',
    image_url VARCHAR(500) NOT NULL COMMENT '图片URL',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    INDEX idx_user_date (user_id, diary_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日记图片表';
