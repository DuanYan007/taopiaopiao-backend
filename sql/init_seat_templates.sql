-- ====================================================================
-- 座位模板初始化数据
-- 创建日期: 2026-02-23
-- 说明: 根据现有场馆数据构造座位模板
-- ====================================================================

-- 插入座位模板数据
INSERT INTO seat_templates (name, venue_id, template_code, total_rows, total_seats, layout_type, layout_data, status) VALUES
-- 1. 国家大剧院 - 歌剧院模板（VIP分区）
('国家大剧院-歌剧院标准模板', 1, 'NCP_THEATER_001', 15, 1800, 2,
 '{
   "version": "1.0",
   "areas": [
     {
       "areaCode": "VIP",
       "areaName": "VIP区",
       "tierId": null,
       "price": 1280,
       "color": "#FFD700",
       "rows": [
         {"rowNum": 1, "rowLabel": "1排", "startSeat": 1, "endSeat": 30, "seatGap": 0},
         {"rowNum": 2, "rowLabel": "2排", "startSeat": 1, "endSeat": 30, "seatGap": 0},
         {"rowNum": 3, "rowLabel": "3排", "startSeat": 1, "endSeat": 30, "seatGap": 0}
       ]
     },
     {
       "areaCode": "A",
       "areaName": "一楼A区",
       "tierId": null,
       "price": 680,
       "color": "#87CEEB",
       "rows": [
         {"rowNum": 4, "rowLabel": "4排", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "5排", "startSeat": 1, "endSeat": 25, "seatGap": 0}
       ]
     },
     {
       "areaCode": "B",
       "areaName": "一楼B区",
       "tierId": null,
       "price": 680,
       "color": "#87CEEB",
       "rows": [
         {"rowNum": 4, "rowLabel": "4排", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "5排", "startSeat": 1, "endSeat": 25, "seatGap": 0}
       ]
     },
     {
       "areaCode": "C",
       "areaName": "二楼看台",
       "tierId": null,
       "price": 380,
       "color": "#98FB98",
       "rows": [
         {"rowNum": 6, "rowLabel": "6排", "startSeat": 1, "endSeat": 40, "seatGap": 0},
         {"rowNum": 7, "rowLabel": "7排", "startSeat": 1, "endSeat": 40, "seatGap": 0},
         {"rowNum": 8, "rowLabel": "8排", "startSeat": 1, "endSeat": 40, "seatGap": 0},
         {"rowNum": 9, "rowLabel": "9排", "startSeat": 1, "endSeat": 40, "seatGap": 0},
         {"rowNum": 10, "rowLabel": "10排", "startSeat": 1, "endSeat": 40, "seatGap": 0}
       ]
     }
   ],
   "stage": {
     "type": "standard",
     "position": "top"
   }
 }', 1),

-- 2. 梅赛德斯-奔驰文化中心 - 演唱会模板（多档分区）
('梅奔中心-演唱会标准模板', 2, 'BENZ_CENTER_CONCERT_001', 25, 3000, 3,
 '{
   "version": "1.0",
   "areas": [
     {
       "areaCode": "SVIP",
       "areaName": "超级VIP",
       "tierId": null,
       "price": 2880,
       "color": "#FF4500",
       "rows": [
         {"rowNum": 1, "rowLabel": "VIP1排", "startSeat": 1, "endSeat": 20, "seatGap": 2}
       ]
     },
     {
       "areaCode": "VIP",
       "areaName": "VIP区",
       "tierId": null,
       "price": 1680,
       "color": "#FFD700",
       "rows": [
         {"rowNum": 2, "rowLabel": "VIP2排", "startSeat": 1, "endSeat": 30, "seatGap": 0},
         {"rowNum": 3, "rowLabel": "VIP3排", "startSeat": 1, "endSeat": 30, "seatGap": 0}
       ]
     },
     {
       "areaCode": "A",
       "areaName": "内场A区",
       "tierId": null,
       "price": 1280,
       "color": "#FFA500",
       "rows": [
         {"rowNum": 4, "rowLabel": "A1", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "A2", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 6, "rowLabel": "A3", "startSeat": 1, "endSeat": 25, "seatGap": 0}
       ]
     },
     {
       "areaCode": "B",
       "areaName": "内场B区",
       "tierId": null,
       "price": 1280,
       "color": "#FFA500",
       "rows": [
         {"rowNum": 4, "rowLabel": "B1", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "B2", "startSeat": 1, "endSeat": 25, "seatGap": 0},
         {"rowNum": 6, "rowLabel": "B3", "startSeat": 1, "endSeat": 25, "seatGap": 0}
       ]
     },
     {
       "areaCode": "STAND",
       "areaName": "看台区",
       "tierId": null,
       "price": 580,
       "color": "#ADD8E6",
       "rows": [
         {"rowNum": 7, "rowLabel": "看台1排", "startSeat": 1, "endSeat": 50, "seatGap": 0},
         {"rowNum": 8, "rowLabel": "看台2排", "startSeat": 1, "endSeat": 50, "seatGap": 0},
         {"rowNum": 9, "rowLabel": "看台3排", "startSeat": 1, "endSeat": 50, "seatGap": 0}
       ]
     }
   ],
   "stage": {
     "type": "standard",
     "position": "top"
   }
 }', 1),

-- 3. 广州大剧院 - 话剧模板（普通布局）
('广州大剧院-话剧标准模板', 3, 'GZ_THEATER_DRAMA_001', 12, 800, 1,
 '{
   "version": "1.0",
   "areas": [
     {
       "areaCode": "STALL",
       "areaName": "一楼池座",
       "tierId": null,
       "price": 480,
       "color": "#90EE90",
       "rows": [
         {"rowNum": 1, "rowLabel": "1排", "startSeat": 1, "endSeat": 22, "seatGap": 0},
         {"rowNum": 2, "rowLabel": "2排", "startSeat": 1, "endSeat": 22, "seatGap": 0},
         {"rowNum": 3, "rowLabel": "3排", "startSeat": 1, "endSeat": 22, "seatGap": 0},
         {"rowNum": 4, "rowLabel": "4排", "startSeat": 1, "endSeat": 22, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "5排", "startSeat": 1, "endSeat": 22, "seatGap": 0}
       ]
     },
     {
       "areaCode": "BALCONY",
       "areaName": "二楼楼座",
       "tierId": null,
       "price": 380,
       "color": "#DDA0DD",
       "rows": [
         {"rowNum": 6, "rowLabel": "6排", "startSeat": 1, "endSeat": 24, "seatGap": 0},
         {"rowNum": 7, "rowLabel": "7排", "startSeat": 1, "endSeat": 24, "seatGap": 0},
         {"rowNum": 8, "rowLabel": "8排", "startSeat": 1, "endSeat": 24, "seatGap": 0}
       ]
     }
   ],
   "stage": {
     "type": "thrust",
     "position": "center"
   }
 }', 1),

-- 4. 深圳湾体育中心 - 体育场演唱会模板（大型）
('深圳湾中心-体育场演唱会模板', 4, 'SZ_BAY_STADIUM_001', 30, 5000, 3,
 '{
   "version": "1.0",
   "areas": [
     {
       "areaCode": "FIELD",
       "areaName": "内场",
       "tierId": null,
       "price": 1880,
       "color": "#FF69B4",
       "rows": [
         {"rowNum": 1, "rowLabel": "内场1区", "startSeat": 1, "endSeat": 100, "seatGap": 0},
         {"rowNum": 2, "rowLabel": "内场2区", "startSeat": 1, "endSeat": 100, "seatGap": 0}
       ]
     },
     {
       "areaCode": "STAND_A",
       "areaName": "看台A区",
       "tierId": null,
       "price": 980,
       "color": "#FFA07A",
       "rows": [
         {"rowNum": 3, "rowLabel": "A区1排", "startSeat": 1, "endSeat": 50, "seatGap": 0},
         {"rowNum": 4, "rowLabel": "A区2排", "startSeat": 1, "endSeat": 50, "seatGap": 0}
       ]
     },
     {
       "areaCode": "STAND_B",
       "areaName": "看台B区",
       "tierId": null,
       "price": 980,
       "color": "#FFA07A",
       "rows": [
         {"rowNum": 3, "rowLabel": "B区1排", "startSeat": 1, "endSeat": 50, "seatGap": 0},
         {"rowNum": 4, "rowLabel": "B区2排", "startSeat": 1, "endSeat": 50, "seatGap": 0}
       ]
     },
     {
       "areaCode": "STAND_C",
       "areaName": "看台C区",
       "tierId": null,
       "price": 580,
       "color": "#87CEFA",
       "rows": [
         {"rowNum": 5, "rowLabel": "C区1排", "startSeat": 1, "endSeat": 80, "seatGap": 0},
         {"rowNum": 6, "rowLabel": "C区2排", "startSeat": 1, "endSeat": 80, "seatGap": 0}
       ]
     }
   ],
   "stage": {
     "type": "arena",
     "position": "center"
   }
 }', 1),

-- 5. 杭州奥体中心 - 大型体育场模板
('杭州奥体-大莲花体育场模板', 5, "HZ_Olympic_STADIUM_001", 40, 8000, 3,
 '{
   "version": "1.0",
   "areas": [
     {
       "areaCode": "VVIP",
       "areaName": "主席台/VVIP",
       "tierId": null,
       "price": 3880,
       "color": "#8B0000",
       "rows": [
         {"rowNum": 1, "rowLabel": "VVIP1排", "startSeat": 1, "endSeat": 30, "seatGap": 2}
       ]
     },
     {
       "areaCode": "VIP",
       "areaName": "VIP包厢区",
       "tierId": null,
       "price": 2880,
       "color": "#FFD700",
       "rows": [
         {"rowNum": 2, "rowLabel": "VIP1排", "startSeat": 1, "endSeat": 50, "seatGap": 0},
         {"rowNum": 3, "rowLabel": "VIP2排", "startSeat": 1, "endSeat": 50, "seatGap": 0}
       ]
     },
     {
       "areaCode": "NORTH",
       "areaName": "北看台",
       "tierId": null,
       "price": 880,
       "color": "#4682B4",
       "rows": [
         {"rowNum": 4, "rowLabel": "北1区", "startSeat": 1, "endSeat": 100, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "北2区", "startSeat": 1, "endSeat": 100, "seatGap": 0}
       ]
     },
     {
       "areaCode": "SOUTH",
       "areaName": "南看台",
       "tierId": null,
       "price": 680,
       "color": "#32CD32",
       "rows": [
         {"rowNum": 4, "rowLabel": "南1区", "startSeat": 1, "endSeat": 100, "seatGap": 0},
         {"rowNum": 5, "rowLabel": "南2区", "startSeat": 1, "endSeat": 100, "seatGap": 0}
       ]
     }
   ],
   "stage": {
     "type": "arena",
     "position": "center"
   }
 }', 1);

-- ====================================================================
-- 数据说明
-- ====================================================================
--
-- 1. 模板与场馆对应关系:
--    - 国家大剧院(id=1): 歌剧院模板，VIP分区布局
--    - 梅赛德斯-奔驰文化中心(id=2): 演唱会模板，多档分区
--    - 广州大剧院(id=3): 话剧模板，普通布局
--    - 深圳湾体育中心(id=4): 体育场演唱会模板
--    - 杭州奥体中心(id=5): 大型体育场模板
--
-- 2. 布局类型说明:
--    - layout_type = 1: 普通（统一价或简单分区）
--    - layout_type = 2: VIP分区（明确的VIP/普通区域划分）
--    - layout_type = 3: 混合（多种票档混合布局）
--    - layout_type = 4: 自定义
--
-- 3. template_code 命名规范:
--    - 格式: {场馆简称}_{类型}_{序号}
--    - 示例: NCP_THEATER_001 (国家大剧院歌剧院)
--
-- 4. 执行方式:
--    mysql -u root -p7566 --default-character-set=utf8mb4 taopiaopiao < sql/init_seat_templates.sql
--
-- ====================================================================
