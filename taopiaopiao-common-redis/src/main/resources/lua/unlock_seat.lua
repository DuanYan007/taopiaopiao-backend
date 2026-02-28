-- 释放座位脚本
-- 参数: KEYS[1]=sessionId, ARGV[1]=userId, ARGV[2]=seatCount, ARGV[3..]=seats
-- 返回: 释放的座位数量

local sessionId = KEYS[1]
local userId = ARGV[1]
local seatCount = tonumber(ARGV[2])
local userLockKey = "user:" .. userId .. ":locks"
local unlocked = 0

for i = 3, 3 + seatCount - 1 do
    local seatId = ARGV[i]
    local seatKey = "seat:" .. sessionId .. ":" .. seatId

    if redis.call("HDEL", userLockKey, seatId) == 1 then
        redis.call("SET", seatKey, 0)
        unlocked = unlocked + 1
    end
end

return unlocked
