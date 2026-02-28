-- 锁座脚本
-- 参数: KEYS[1]=sessionId, ARGV[1]=userId, ARGV[2]=seatCount, ARGV[3]=expireSeconds, ARGV[4..]=seats
-- 返回: 0=成功, 1=座位不存在, 2=座位不可用, 3=重复购票

local sessionId = KEYS[1]
local userId = ARGV[1]
local seatCount = tonumber(ARGV[2])
local expireSeconds = tonumber(ARGV[3])
local userLockKey = "user:" .. userId .. ":locks"

-- 检查重复购票
for i = 4, 4 + seatCount - 1 do
    if redis.call("HEXISTS", userLockKey, ARGV[i]) == 1 then
        return 3
    end
end

-- 执行锁座
for i = 4, 4 + seatCount - 1 do
    local seatId = ARGV[i]
    local seatKey = "seat:" .. sessionId .. ":" .. seatId
    local current = tonumber(redis.call("GET", seatKey))

    if current == nil then
        return 1  -- 座位不存在
    end

    if current ~= 0 then
        return 2  -- 座位不可用
    end

    redis.call("SET", seatKey, 1, "EX", expireSeconds)
    redis.call("HSET", userLockKey, seatId, tostring(ARGV[4 + seatCount + i - 4]))
end

return 0
