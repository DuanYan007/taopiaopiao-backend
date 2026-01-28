-- 场馆测试数据
-- 包含10个不同城市的场馆，涵盖剧院、体育场、音乐厅等类型

INSERT INTO venues (name, city, district, address, latitude, longitude, capacity, facilities, description, images, metadata) VALUES
('国家大剧院', '北京', '西城区', '西城区西长安街2号', 39.903738, 116.391432, 5452,
 '["停车场", "无障碍设施", "VIP包厢", "餐厅", "咖啡厅", "纪念品商店", "同声传译设备", "充电宝租赁"]',
 '中国国家表演艺术中心，亚洲最大的剧院综合体之一，外形如同一颗巨大的半透明蛋体，是北京的地标性建筑。拥有歌剧院、音乐厅、戏剧场和小剧场四个主要演出场地。',
 '["https://example.com/images/ncpa_1.jpg", "https://example.com/images/ncpa_2.jpg", "https://example.com/images/ncpa_3.jpg"]',
 '{"open_time": "09:00-21:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "10元/小时", "subway": "1号线天安门西站"}'),

('梅赛德斯-奔驰文化中心', '上海', '浦东新区', '浦东新区世博大道1200号', 31.187263, 121.499329, 18000,
 '["停车场", "无障碍通道", "VIP休息室", "餐厅", "酒吧", "商店", "免费WiFi", "寄存服务"]',
 '上海世博园永久性场馆之一，外形如同一艘巨大的太空飞船，是亚洲顶级的综合性文化演艺场馆。可举办演唱会、体育赛事、企业活动等各类大型活动。',
 '["https://example.com/images/mercedes_arena_1.jpg", "https://example.com/images/mercedes_arena_2.jpg"]',
 '{"open_time": "10:00-22:00", "ticket_types": ["内场票", "看台票", "VIP票"], "parking_fee": "15元/小时", "subway": "8号线中华艺术宫站"}'),

('广州大剧院', '广州', '天河区', '天河区珠江新城华夏路', 23.115869, 113.328721, 4200,
 '["地下停车场", "无障碍设施", "贵宾厅", "咖啡厅", "书店", "ATM", "免费WiFi"]',
 '由世界著名建筑师扎哈·哈迪德设计，外形宛如两块被珠江水冲刷过的灵石，是华南地区最具影响力的综合艺术剧院之一。',
 '["https://example.com/images/gz_opera_1.jpg", "https://example.com/images/gz_opera_2.jpg", "https://example.com/images/gz_opera_3.jpg"]',
 '{"open_time": "09:00-21:00", "ticket_types": ["普通票", "VIP票", "学生票", "老年票"], "parking_fee": "8元/小时", "subway": "3号线珠江新城站"}'),

('深圳湾体育中心"春茧"', '深圳', '南山区', '南山区滨海大道3001号', 22.484569, 113.942908, 15000,
 '["大型停车场", "无障碍设施", "VIP包厢", "餐厅", "健身房", "游泳馆", "便利店", "充电桩"]',
 '深圳大运会主场馆，因外形酷似巨大的春茧而得名。是一个集体育比赛、演唱会、大型活动于一体的综合性场馆。',
 '["https://example.com/images/sz_bay_arena_1.jpg", "https://example.com/images/sz_bay_arena_2.jpg"]',
 '{"open_time": "08:00-22:00", "ticket_types": ["普通票", "VIP票", "包厢票"], "parking_fee": "10元/小时", "subway": "2号线科苑站"}'),

('杭州奥体中心主体育场(大莲花)', '杭州', '萧山区', '萧山区奔竞大道223号', 30.226578, 120.204324, 80892,
 '["超大停车场", "无障碍设施", "VIP包厢", "餐饮区", "医疗点", "警务室", "便利店", "免费WiFi"]',
 '2022年杭州亚运会主场馆，因外形设计酷似一朵盛开的莲花而得名，可容纳8万多人，是浙江省规模最大的体育场。',
 '["https://example.com/images/hz_olympic_1.jpg", "https://example.com/images/hz_olympic_2.jpg", "https://example.com/images/hz_olympic_3.jpg"]',
 '{"open_time": "09:00-18:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "5元/小时", "subway": "6号线奥体中心站"}'),

('成都东郊记忆演艺中心', '成都', '成华区', '成华区建设南路中段4号', 30.662497, 104.096569, 3500,
 '["停车场", "无障碍通道", "VIP休息区", "咖啡厅", "文创商店", "免费WiFi", "摄影服务"]',
 '由原国营红光电子管厂旧址改建而成，融合工业遗风与现代艺术，是成都最具特色的文化演艺场所之一，经常举办音乐演出、话剧、展览等活动。',
 '["https://example.com/images/cd_memory_1.jpg", "https://example.com/images/cd_memory_2.jpg"]',
 '{"open_time": "10:00-22:00", "ticket_types": ["站票", "坐票", "VIP票"], "parking_fee": "6元/小时", "subway": "8号线东郊记忆站"}'),

('武汉琴台大剧院', '武汉', '汉阳区', '汉阳区知音大道月湖畔', 30.553967, 114.279067, 4200,
 '["地下停车场", "无障碍设施", "贵宾厅", "咖啡厅", "茶室", "书店", "免费WiFi"]',
 '武汉地标性文化建筑，位于月湖之畔，与琴台美术馆、琴台音乐厅形成文化三角区。剧院设计典雅，音响效果一流，是观赏大型演出的理想场所。',
 '["https://example.com/images/wh_qintai_1.jpg", "https://example.com/images/wh_qintai_2.jpg", "https://example.com/images/wh_qintai_3.jpg"]',
 '{"open_time": "09:00-21:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "8元/小时", "subway": "6号线琴台站"}'),

('西安奥体中心体育场', '西安', '灞桥区', '灞桥区港务西路', 34.389876, 109.017543, 60000,
 '["大型停车场", "无障碍通道", "VIP包厢", "餐饮区", "医疗站", "便利店", "充电桩", "免费WiFi"]',
 '2021年第十四届全国运动会主场馆，建筑风格融入唐风元素，寓意"丝路启航，盛世之花"。是西北地区规模最大、设施最先进的体育场馆。',
 '["https://example.com/images/xa_olympic_1.jpg", "https://example.com/images/xa_olympic_2.jpg"]',
 '{"open_time": "08:00-20:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "5元/小时", "subway": "14号线奥体中心站"}'),

('南京奥体中心体育馆', '南京', '建邺区', '建邺区江东中路222号', 32.009876, 118.729876, 20000,
 '["地下停车场", "无障碍设施", "VIP包厢", "餐厅", "商店", "ATM", "医疗点", "免费WiFi"]',
 '2005年十运会主场馆，2014年青运会主场馆，由主体育场、体育馆、游泳馆、网球中心等组成，是江苏省规模最大的综合性体育场馆群。',
 '["https://example.com/images/nj_olympic_1.jpg", "https://example.com/images/nj_olympic_2.jpg", "https://example.com/images/nj_olympic_3.jpg"]',
 '{"open_time": "09:00-21:00", "ticket_types": ["普通票", "VIP票", "包厢票"], "parking_fee": "8元/小时", "subway": "2号线奥体东站"}'),

('重庆国际博览中心', '重庆', '渝北区', '渝北区悦来大道66号', 29.718876, 106.527654, 25000,
 '["超大停车场", "无障碍设施", "VIP休息室", "餐饮区", "咖啡厅", "便利店", "充电桩", "免费WiFi", "婴儿车租赁"]',
 '西部最大、全国第二大的现代化展览中心，设计灵感源于蝴蝶，外形优美。可举办大型演唱会、展览、会议等活动。',
 '["https://example.com/images/cq_expo_1.jpg", "https://example.com/images/cq_expo_2.jpg"]',
 '{"open_time": "09:00-18:00", "ticket_types": ["普通票", "VIP票", "学生票"], "parking_fee": "6元/小时", "subway": "6号线/10号线悦来站"}');
