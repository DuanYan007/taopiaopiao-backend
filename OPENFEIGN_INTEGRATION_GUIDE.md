# OpenFeign 集成指南

本文档说明如何为 session-service 集成 OpenFeign，实现调用 venue-service 和 event-service 的功能。

---

## 一、添加依赖

### 1.1 修改父 pom.xml

在 `dependencyManagement` 中添加（大约在第 202 行，`</dependencies>` 标签前）：

```xml
        <!-- OpenFeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>4.1.0</version>
        </dependency>

        <!-- Loadbalancer -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
            <version>4.1.0</version>
        </dependency>
```

### 1.2 修改 session-service-application/pom.xml

在 Nacos 服务发现依赖后添加（大约在第 45 行后）：

```xml
        <!-- OpenFeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
            <version>4.1.0</version>
        </dependency>

        <!-- Loadbalancer -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
            <version>4.1.0</version>
        </dependency>

        <!-- 依赖其他服务的 API 模块 -->
        <dependency>
            <groupId>com.duanyan</groupId>
            <artifactId>taopiaopiao-venue-service-api</artifactId>
        </dependency>
        <dependency>
            <groupId>com.duanyan</groupId>
            <artifactId>taopiaopiao-event-service-api</artifactId>
        </dependency>
```

---

## 二、创建 Feign Client 接口

### 2.1 创建目录结构

在 `taopiaopiao-session-service-application/src/main/java/com/duanyan/taopiaopiao/sessionservice/application/` 下创建 `client` 目录。

### 2.2 创建 VenueClient.java

文件路径：`.../application/client/VenueClient.java`

```java
package com.duanyan.taopiaopiao.sessionservice.application.client;

import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "venue-service", path = "/admin/venues")
public interface VenueClient {

    @GetMapping("/{id}")
    VenueResponse getVenueById(@PathVariable("id") Long id);
}
```

### 2.3 创建 EventClient.java

文件路径：`.../application/client/EventClient.java`

```java
package com.duanyan.taopiaopiao.sessionservice.application.client;

import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", path = "/admin/events")
public interface EventClient {

    @GetMapping("/{id}")
    EventResponse getEventById(@PathVariable("id") Long id);
}
```

---

## 三、创建 Feign 配置类（处理 Result 包装）

### 3.1 创建 config 目录

在 `.../application/` 下创建 `config` 目录。

### 3.2 创建 FeignResultWrapper.java

文件路径：`.../application/config/FeignResultWrapper.java`

```java
package com.duanyan.taopiaopiao.sessionservice.application.config;

import com.duanyan.taopiaopiao.common.response.Result;
import feign.FeignException;
import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;

@Slf4j
@Configuration
public class FeignResultWrapper {

    @Bean
    public Decoder resultDecoder() {
        return (response, type) -> {
            // 直接使用 ResponseEntity 或处理 Result 包装
            // 这里简化处理，让 Feign 直接返回 Result，在业务层提取 data
            return new Decoder.Default().decode(response, type);
        };
    }
}
```

**注意**：由于其他服务返回的是 `Result<T>` 包装类型，有两种处理方式：

**方式一（推荐）**：在 Feign Client 中直接返回 `Result<VenueResponse>`，然后在 Service 层手动提取 data

**方式二**：创建自定义 Decoder（较复杂，容易产生循环依赖）

建议使用方式一，修改 Client 接口：

```java
@FeignClient(name = "venue-service", path = "/admin/venues")
public interface VenueClient {
    @GetMapping("/{id}")
    Result<VenueResponse> getVenueById(@PathVariable("id") Long id);
}
```

---

## 四、修改 SessionServiceImpl

### 4.1 添加导入

在现有的 import 后添加：

```java
import com.duanyan.taopiaopiao.common.response.Result;
import com.duanyan.taopiaopiao.eventservice.api.dto.EventResponse;
import com.duanyan.taopiaopiao.sessionservice.application.client.EventClient;
import com.duanyan.taopiaopiao.sessionservice.application.client.VenueClient;
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
```

### 4.2 添加注入字段

在类中添加：

```java
private final VenueClient venueClient;
private final EventClient eventClient;
```

### 4.3 修改 getSessionPage 方法

在分页查询后、转换为 DTO 前，添加获取关联信息的代码：

```java
// 分页查询
Page<Session> page = new Page<>(request.getPage(), request.getPageSize());
IPage<Session> sessionPage = sessionMapper.selectPage(page, queryWrapper);

// 新增：收集需要查询的 venueId 和 eventId
List<Long> venueIds = sessionPage.getRecords().stream()
        .map(Session::getVenueId)
        .filter(id -> id != null)
        .distinct()
        .toList();
List<Long> eventIds = sessionPage.getRecords().stream()
        .map(Session::getEventId)
        .filter(id -> id != null)
        .distinct()
        .toList();

// 新增：批量查询关联信息
Map<Long, VenueResponse> venueMap = fetchVenuesByIds(venueIds);
Map<Long, EventResponse> eventMap = fetchEventsByIds(eventIds);

// 修改：转换时传入关联信息
List<SessionResponse> sessionResponseList = sessionPage.getRecords().stream()
        .map(session -> convertToResponse(session, venueMap, eventMap))
        .collect(Collectors.toList());
```

### 4.4 修改 getSessionById 方法

```java
@Override
public SessionResponse getSessionById(Long id) {
    Session session = sessionMapper.selectById(id);
    if (session == null) {
        throw new BusinessException(404, "场次不存在");
    }

    // 新增：查询关联信息
    Map<Long, VenueResponse> venueMap = fetchVenuesByIds(List.of(session.getVenueId()));
    Map<Long, EventResponse> eventMap = fetchEventsByIds(List.of(session.getEventId()));

    return convertToResponse(session, venueMap, eventMap);
}
```

### 4.5 添加辅助方法

在类的最后添加以下方法：

```java
/**
 * 转换为响应DTO（带关联信息）
 */
private SessionResponse convertToResponse(Session session,
                                           Map<Long, VenueResponse> venueMap,
                                           Map<Long, EventResponse> eventMap) {
    SessionResponse response = new SessionResponse();
    BeanUtils.copyProperties(session, response);

    // 填充演出名称
    if (session.getEventId() != null && eventMap != null) {
        EventResponse event = eventMap.get(session.getEventId());
        if (event != null) {
            response.setEventName(event.getName());
        }
    }

    // 填充场馆名称
    if (session.getVenueId() != null && venueMap != null) {
        VenueResponse venue = venueMap.get(session.getVenueId());
        if (venue != null) {
            response.setVenueName(venue.getName());
        }
    }

    // 处理metadata
    if (StringUtils.hasText(session.getMetadata())) {
        try {
            response.setMetadata(objectMapper.readValue(
                    session.getMetadata(),
                    SessionResponse.SessionMetadata.class
            ));
        } catch (Exception e) {
            log.error("metadata解析失败: {}", e.getMessage());
        }
    }

    response.setTicketTiers(generateTicketTierInfo(session));
    return response;
}

/**
 * 保留原方法，兼容其他调用
 */
private SessionResponse convertToResponse(Session session) {
    return convertToResponse(session, null, null);
}

/**
 * 批量获取场馆信息
 */
private Map<Long, VenueResponse> fetchVenuesByIds(List<Long> venueIds) {
    if (venueIds == null || venueIds.isEmpty()) {
        return Map.of();
    }

    Map<Long, VenueResponse> result = new HashMap<>();
    for (Long id : venueIds) {
        try {
            Result<VenueResponse> resp = venueClient.getVenueById(id);
            if (resp != null && resp.getCode() == 200 && resp.getData() != null) {
                result.put(id, resp.getData());
            }
        } catch (Exception e) {
            log.error("查询场馆信息失败, venueId: {}, error: {}", id, e.getMessage());
        }
    }
    return result;
}

/**
 * 批量获取演出信息
 */
private Map<Long, EventResponse> fetchEventsByIds(List<Long> eventIds) {
    if (eventIds == null || eventIds.isEmpty()) {
        return Map.of();
    }

    Map<Long, EventResponse> result = new HashMap<>();
    for (Long id : eventIds) {
        try {
            Result<EventResponse> resp = eventClient.getEventById(id);
            if (resp != null && resp.getCode() == 200 && resp.getData() != null) {
                result.put(id, resp.getData());
            }
        } catch (Exception e) {
            log.error("查询演出信息失败, eventId: {}, error: {}", id, e.getMessage());
        }
    }
    return result;
}
```

### 4.6 添加导入

确保添加以下 import：

```java
import java.util.HashMap;
import java.util.Map;
```

---

## 五、验证启动类

确认 `SessionServiceApplication.java` 已有 `@EnableFeignClients` 注解：

```java
@EnableFeignClients(basePackages = {
    "com.duanyan.taopiaopiao.sessionservice.application",
    "com.duanyan.taopiaopiao.common"
})
```

---

## 六、测试

1. 刷新 Maven 项目
2. 先启动 venue-service 和 event-service
3. 再启动 session-service
4. 调用场次查询接口，检查 `eventName` 和 `venueName` 是否有值

---

## 常见问题

### Q1: 循环依赖错误
**解决**：不要创建自定义 Decoder，直接让 Feign 返回 `Result<T>` 类型

### Q2: 找不到其他服务的 API 类
**解决**：确保在 pom.xml 中添加了对其他服务 api 模块的依赖

### Q3: FeignClient 扫描不到
**解决**：检查 `@EnableFeignClients` 的 basePackages 是否正确
