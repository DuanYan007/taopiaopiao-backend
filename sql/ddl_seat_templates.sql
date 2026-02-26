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

-- -------------------------------------------------------------
-- 2. 创建座位模板关联表 (模板与票档的关联关系)
-- -------------------------------------------------------------
CREATE TABLE IF NOT EXISTS seat_template_tier_ref (
    id              BIGINT          AUTO_INCREMENT PRIMARY KEY COMMENT '关联ID',
    template_id     BIGINT          NOT NULL COMMENT '模板ID',
    tier_id         BIGINT          NOT NULL COMMENT '票档ID(tier_id来自ticket_tiers表)',
    area_code       VARCHAR(20)     NOT NULL COMMENT '区域编码',
    area_name       VARCHAR(50)     NOT NULL COMMENT '区域名称',
    price           DECIMAL(10, 2)  NOT NULL COMMENT '该区域价格',
    seat_count      INT             NOT NULL DEFAULT 0 COMMENT '该区域座位数',
    sort_order      INT             NOT NULL DEFAULT 0 COMMENT '排序',
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted         TINYINT         NOT NULL DEFAULT 0,
    INDEX idx_template_id (template_id),
    INDEX idx_tier_id (tier_id),
    UNIQUE KEY uk_template_area (template_id, area_code, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='座位模板票档关联表';

-- -------------------------------------------------------------
-- 3. 修改场次表，增加座位模板关联字段
-- -------------------------------------------------------------
-- MySQL 8.4 不支持 ADD COLUMN IF NOT EXISTS 和 ADD INDEX IF NOT EXISTS
-- 执行前请先检查字段是否存在:
--   SHOW COLUMNS FROM sessions LIKE 'seat_template_id';

-- 如果字段不存在，取消以下两行的注释后执行:
-- ALTER TABLE sessions
-- ADD COLUMN seat_template_id BIGINT NULL COMMENT '座位模板ID' AFTER ticket_tier_config;

-- 如果索引不存在，取消以下两行的注释后执行:
-- ALTER TABLE sessions
-- ADD INDEX idx_seat_template_id (seat_template_id);

-- -------------------------------------------------------------
-- 4. 数据字典说明
-- -------------------------------------------------------------
-- layout_data JSON 结构说明:
-- {
--   "version": "1.0",                    // 布局版本
--   "areas": [                           // 区域列表
--     {
--       "areaCode": "A",                 // 区域编码
--       "areaName": "A区",               // 区域名称
--       "tierId": 1,                     // 关联票档ID
--       "price": 280.00,                 // 区域价格
--       "color": "#FF6B6B",              // 展示颜色
--       "rows": [                        // 行列表
--         {
--           "rowNum": 1,                 // 行号
--           "rowLabel": "1排",           // 行标签
--           "startSeat": 1,              // 起始座位号
--           "endSeat": 20,               // 结束座位号
--           "seatGap": 0,                // 座位间隔(0=无间隔)
--           "seats": [                   // 座位明细(可选)
--             {
--               "seatNum": 1,
--               "seatLabel": "1排1座",
--               "available": true,
--               "type": "normal"         // normal/vip/loveseat
--             }
--           ]
--         }
--       ]
--     }
--   ],
--   "stage": {                           // 舞台信息(可选)
--     "type": "standard",                // standard/thrust/arena
--     "position": "top"                  // top/bottom/left/right
--   }
-- }

-- layout_type 枚举说明:
-- 1 - 普通: 全场统一价或简单分区
-- 2 - VIP分区: 明确的VIP/普通区域划分
-- 3 - 混合: 多种票档混合布局
-- 4 - 自定义: 完全自定义座位布局

-- ====================================================================
-- 执行完成后验证
-- ====================================================================
-- SELECT * FROM seat_templates;
-- SELECT * FROM seat_template_tier_ref;
-- SHOW COLUMNS FROM sessions LIKE 'seat_template_id';
-- ====================================================================
