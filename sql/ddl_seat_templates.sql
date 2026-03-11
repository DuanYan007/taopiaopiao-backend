-- ====================================================================
-- 座位模板服务 - 数据库初始化脚本
-- 创建日期: 2026-02-23
-- MySQL版本: 8.4.8
-- 说明: 为座位模板服务创建必要的表结构
-- ====================================================================

-- -------------------------------------------------------------
-- 1. 创建座位模板主表
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seat_templates (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY COMMENT '模板ID',
    name            VARCHAR(100)    NOT NULL COMMENT '模板名称',
    venue_id        BIGINT          NOT NULL COMMENT '关联场馆ID',
    template_code   VARCHAR(50)     NOT NULL COMMENT '模板编码(唯一标识)',
    total_rows      INT             NOT NULL DEFAULT 0 COMMENT '总行数',
    total_seats     INT             NOT NULL DEFAULT 0 COMMENT '总座位数',
    layout_type     TINYINT         NOT NULL DEFAULT 1 COMMENT '布局类型: 1=普通, 2=VIP分区, 3=混合, 4=自定义',
    layout_data     JSON            NOT NULL COMMENT '座位布局数据(JSON): 包含区域、行列、座位属性等',
    status          TINYINT         NOT NULL DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0=未删除, 1=已删除',
    INDEX idx_venue_id (venue_id),
    INDEX idx_template_code (template_code),
    INDEX idx_status (status),
    UNIQUE KEY uk_template_code (template_code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位模板表';
