-- 4K 电影网站数据库初始化脚本 (MySQL 5.7)
CREATE DATABASE IF NOT EXISTS movie4k_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE movie4k_db;

-- 1. 分类表 (用于影库界面筛选)
CREATE TABLE IF NOT EXISTS `category` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称: 如动作、科幻、大陆、欧美',
    `type` TINYINT DEFAULT 0 COMMENT '0: 题材分类, 1: 地区分类, 2: 语言分类',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 电影主表
CREATE TABLE IF NOT EXISTS `movie` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(255) NOT NULL COMMENT '电影标题',
    `description` TEXT COMMENT '电影简介',
    `cover_url` VARCHAR(500) COMMENT '封面图链接',
    `release_year` INT COMMENT '上映年份',
    `language` VARCHAR(50) COMMENT '语言',
    `country` VARCHAR(50) COMMENT '国家/地区',
    `rating` DECIMAL(3, 1) DEFAULT 0.0 COMMENT '评分',
    `tags` VARCHAR(255) COMMENT '标签，逗号分隔',
    `category_id` INT COMMENT '主分类ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (category_id),
    INDEX idx_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 电影播放资源表 (支持更换资源)
CREATE TABLE IF NOT EXISTS `movie_resource` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `movie_id` INT NOT NULL,
    `source_name` VARCHAR(100) NOT NULL COMMENT '资源名称: 如 4K线路1, 极速资源',
    `play_url` TEXT NOT NULL COMMENT '播放地址或API链接',
    `quality` VARCHAR(20) DEFAULT '4K' COMMENT '清晰度',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 轮播推荐表 (主界面展示)
CREATE TABLE IF NOT EXISTS `carousel` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `movie_id` INT NOT NULL,
    `image_url` VARCHAR(500) NOT NULL COMMENT '轮播大图链接',
    `sort_order` INT DEFAULT 0 COMMENT '排序权重',
    `is_active` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 观影历史记录表
CREATE TABLE IF NOT EXISTS `watch_history` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `movie_id` INT NOT NULL,
    `progress_seconds` INT DEFAULT 0 COMMENT '观看进度（秒）',
    `last_watched_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_movie (movie_id), -- 这里假设本地单用户，一个电影存一条记录
    FOREIGN KEY (movie_id) REFERENCES movie(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化基础分类数据
INSERT INTO `category` (name, type) VALUES 
('动作', 0), ('科幻', 0), ('喜剧', 0), ('爱情', 0), ('恐怖', 0),
('大陆', 1), ('香港', 1), ('美国', 1), ('韩国', 1), ('日本', 1),
('华语', 2), ('英语', 2), ('韩语', 2), ('日语', 2);
