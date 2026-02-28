package com.duanyan.taopiaopiao.sessionservice.application.controller;

import com.duanyan.taopiaopiao.common.response.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis 测试接口
 *
 * @author duanyan
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "Redis 测试", description = "Redis 连接测试接口")
@RestController
@RequestMapping("/api/test/redis")
@RequiredArgsConstructor
public class RedisTestController {

    private final RedissonClient redissonClient;

    @GetMapping("/ping")
    @Operation(summary = "测试 Redis 连接")
    public Result<Map<String, Object>> ping() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 测试基本连接
            redissonClient.getBucket("test:ping").set("pong");
            String pong = (String) redissonClient.getBucket("test:ping").get();

            result.put("connected", true);
            result.put("response", pong);
            result.put("message", "Redis 连接成功");

            return Result.success(result);
        } catch (Exception e) {
            log.error("Redis 连接失败", e);
            result.put("connected", false);
            result.put("error", e.getMessage());
            return Result.fail("Redis 连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/test/set")
    @Operation(summary = "测试写入数据")
    public Result<Map<String, Object>> testSet(@RequestParam String key, @RequestParam String value) {
        try {
            redissonClient.getBucket(key).set(value);
            Map<String, Object> result = new HashMap<>();
            result.put("key", key);
            result.put("value", value);
            return Result.success(result);
        } catch (Exception e) {
            log.error("写入失败", e);
            return Result.fail("写入失败: " + e.getMessage());
        }
    }

    @GetMapping("/test/get")
    @Operation(summary = "测试读取数据")
    public Result<Map<String, Object>> testGet(@RequestParam String key) {
        try {
            Object value = redissonClient.getBucket(key).get();
            Map<String, Object> result = new HashMap<>();
            result.put("key", key);
            result.put("value", value);
            return Result.success(result);
        } catch (Exception e) {
            log.error("读取失败", e);
            return Result.fail("读取失败: " + e.getMessage());
        }
    }
}
