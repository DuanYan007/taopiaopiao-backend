-- 验证场次数据
-- 检查场次数据的完整性和正确性

-- 1. 查看每个演出的场次统计
SELECT
    e.id AS event_id,
    e.name AS event_name,
    e.type AS event_type,
    COUNT(s.id) AS session_count,
    MIN(s.start_time) AS first_session,
    MAX(s.start_time) AS last_session,
    SUM(s.available_seats) AS total_available_seats
FROM events e
LEFT JOIN sessions s ON e.id = s.event_id
GROUP BY e.id, e.name, e.type
ORDER BY e.id;

-- 2. 查看所有场次详情（前10条）
SELECT
    s.id,
    e.name AS event_name,
    s.session_name,
    s.start_time,
    s.venue_id,
    v.name AS venue_name,
    s.hall_name,
    s.total_seats,
    s.available_seats,
    s.status
FROM sessions s
JOIN events e ON s.event_id = e.id
JOIN venues v ON s.venue_id = v.id
ORDER BY s.start_time
LIMIT 10;

-- 3. 按场馆统计场次数量
SELECT
    v.id AS venue_id,
    v.name AS venue_name,
    v.city,
    COUNT(s.id) AS session_count
FROM venues v
LEFT JOIN sessions s ON v.id = s.venue_id
GROUP BY v.id, v.name, v.city
ORDER BY session_count DESC;

-- 4. 按日期统计场次数量
SELECT
    DATE(start_time) AS session_date,
    COUNT(*) AS session_count,
    SUM(CASE WHEN status = 'on_sale' THEN 1 ELSE 0 END) AS on_sale_count,
    SUM(CASE WHEN status = 'sold_out' THEN 1 ELSE 0 END) AS sold_out_count
FROM sessions
GROUP BY DATE(start_time)
ORDER BY session_date;

-- 5. 检查场次状态分布
SELECT
    status,
    COUNT(*) AS count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM sessions), 2) AS percentage
FROM sessions
GROUP BY status
ORDER BY count DESC;
