-- 票档测试数据
-- 根据 init_events.sql 中的演出数据，为每个演出创建3-5个票档

-- 1. 周杰伦2024嘉年华世界巡回演唱会-上海站 (event_id=1)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(1, 'VIP内场', 2580.00, '#FF5722', '内场前排最佳视角，含专辑CD+海报礼包', 0, 2, TRUE),
(1, '一等座', 1880.00, '#2196F3', '内场后排区域', 1, 4, TRUE),
(1, '二等座', 1280.00, '#4CAF50', '看台前排区域', 2, 6, TRUE),
(1, '三等座', 880.00, '#FF9800', '看台中排区域', 3, 8, TRUE),
(1, '四等座', 580.00, '#9E9E9E', '看台后排区域', 4, 10, TRUE);

-- 2. 开心麻花爆笑舞台《乌龙山伯爵》 (event_id=2)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(2, 'VIP前排', 680.00, '#FF5722', '前排VIP座位，含签名海报', 0, 4, TRUE),
(2, '一等座', 480.00, '#2196F3', '前中区座位', 1, 6, TRUE),
(2, '二等座', 380.00, '#4CAF50', '中区座位', 2, 8, TRUE),
(2, '三等座', 280.00, '#FF9800', '后排座位', 3, 10, TRUE);

-- 3. 梵高星空沉浸式艺术展 (event_id=3)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(3, '成人通票', 198.00, '#2196F3', '成人全日通票', 0, 10, TRUE),
(3, '学生票', 128.00, '#4CAF50', '持学生证购买，限本人使用', 1, 2, TRUE),
(3, '亲子套票(2大1小)', 498.00, '#FF5722', '两位成人+一位儿童', 2, 5, TRUE);

-- 4. CBA总决赛：广东东莞银行vs辽宁本钢 (event_id=4)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(4, 'VIP包厢', 3880.00, '#FF5722', 'VIP包厢，含餐饮服务', 0, 10, TRUE),
(4, ' courtside', 2880.00, '#2196F3', '场边第一排座位', 1, 4, TRUE),
(4, '内场票', 1680.00, '#4CAF50', '内场区域', 2, 6, TRUE),
(4, '看台票', 880.00, '#FF9800', '看台区域', 3, 10, TRUE),
(4, '学生票', 380.00, '#9E9E9E', '持学生证购买', 4, 2, TRUE);

-- 5. 草莓音乐节2024北京站 (event_id=5)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(5, 'VIP三天通票', 1680.00, '#FF5722', 'VIP区域，含休息区+快速通道', 0, 4, TRUE),
(5, '三天通票', 880.00, '#2196F3', '三日通票，可多次进出', 1, 10, TRUE),
(5, '单日票', 380.00, '#4CAF50', '单日入场票', 2, 10, TRUE),
(5, '学生单日票', 280.00, '#FF9800', '持学生证购买，单日有效', 3, 2, TRUE);

-- 6. 迪士尼《冰雪奇缘》音乐剧 (event_id=6)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(6, 'VIP家庭套票', 1280.00, '#FF5722', '2大2小套票，含周边礼包', 0, 5, TRUE),
(6, '一等座', 680.00, '#2196F3', '前区域座位', 1, 6, TRUE),
(6, '二等座', 480.00, '#4CAF50', '中区域座位', 2, 8, TRUE),
(6, '三等座', 380.00, '#FF9800', '后区域座位', 3, 10, TRUE),
(6, '儿童票', 280.00, '#9C27B0', '1.2米以下儿童（需成人陪同）', 4, 4, TRUE);

-- 7. 杨丽萍舞剧《孔雀》十周年纪念版 (event_id=7)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(7, 'VIP席', 1280.00, '#FF5722', '前排VIP座位，含节目册', 0, 4, TRUE),
(7, '一等座', 880.00, '#2196F3', '前排区域', 1, 6, TRUE),
(7, '二等座', 580.00, '#4CAF50', '中排区域', 2, 8, TRUE),
(7, '三等座', 380.00, '#FF9800', '后排区域', 3, 10, TRUE);

-- 8. 2024国际网球公开赛上海大师赛 (event_id=8)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(8, 'VIP全程套票', 3880.00, '#FF5722', '全程VIP席位，含餐饮+休息室', 0, 10, TRUE),
(8, '中央场馆票', 1880.00, '#2196F3', '中央场馆座位', 1, 6, TRUE),
(8, '单日场票', 580.00, '#4CAF50', '单日场票，看台座位', 2, 10, TRUE),
(8, '学生票', 280.00, '#FF9800', '持学生证购买', 3, 2, TRUE);

-- 9. 林俊杰JJ20世界巡回演唱会-成都站 (event_id=9)
INSERT INTO ticket_tiers (event_id, name, price, color, description, sort_order, max_purchase, is_active) VALUES
(9, 'VIP内场', 2280.00, '#FF5722', '内场前排，含应援棒+专辑', 0, 2, TRUE),
(9, '看台VIP', 1680.00, '#2196F3', '看台前排VIP区域', 1, 4, TRUE),
(9, '一等座', 1280.00, '#4CAF50', '看台前排', 2, 6, TRUE),
(9, '二等座', 880.00, '#FF9800', '看台中排', 3, 8, TRUE),
(9, '三等座', 580.00, '#9E9E9E', '看台后排', 4, 10, TRUE);

