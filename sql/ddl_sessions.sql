CREATE TABLE sessions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  event_id BIGINT NOT NULL COMMENT '所属演出ID',
  session_name VARCHAR(200) COMMENT '场次名称',
  start_time DATETIME NOT NULL COMMENT '场次开始时间',
  end_time DATETIME COMMENT '场次结束时间',
  address VARCHAR(500) COMMENT '详细地址',
  available_seats INT NOT NULL DEFAULT 0 COMMENT '可售座位数',
  sold_seats INT NOT NULL DEFAULT 0 COMMENT '已售座位数',
  locked_seats INT NOT NULL DEFAULT 0 COMMENT '锁定座位数',
  status VARCHAR(20) NOT NULL DEFAULT 'not_started' COMMENT '状态: not_started, on_sale, sold_out, ended, off_sale',
  seat_template_id BIGINT COMMENT '座位模板ID',
  metadata JSON COMMENT '扩展字段',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_event_id (event_id),
  INDEX idx_start_time (start_time),
  INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='场次表';
