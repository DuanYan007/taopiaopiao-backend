-- 场馆测试数据
-- 包含10个不同城市的场馆，涵盖剧院、体育场、音乐厅等类型

INSERT INTO venues (name, city, district, address, latitude, longitude, capacity, facilities, description, images, metadata) VALUES
('国家大剧院', '北京', '西城区', '西城区西长安街2号', 39.903738, 116.391432, 5452,
 '["停车场", "无障碍设施", "VIP包厢", "餐厅", "咖啡厅", "纪念品商店", "同声传译设备", "充电宝租赁"]',
 '中国国家表演艺术中心，亚洲最大的剧院综合体之一，外形如同一颗巨大的半透明蛋体，是北京的地标性建筑。拥有歌剧院、音乐厅、戏剧场和小剧场四个主要演出场地。',
 '["https://example.com/images/ncpa_1.jpg", "https://example.com/images/ncpa_2.jpg", "https://example.com/images/ncpa_3.jpg"]',
 '{"open_time": "09:00-21:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "10元/小时", "subway": "1号线天安门西站"}')；
