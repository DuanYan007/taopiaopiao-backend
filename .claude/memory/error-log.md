# 错误日志 - 淘票票后端项目

记录已发生的错误、踩坑和禁止模式。

---

## 2026-02-12 - Gateway Spring MVC/WebFlux 冲突

### 错误描述
启动 Gateway 时报错：`Spring MVC found on classpath, which is incompatible with Spring Cloud Gateway`

### 根本原因
`taopiaopiao-common-web` 模块包含 `spring-boot-starter-web` 依赖，被 Gateway 通过传递性依赖引入

### 解决方案
拆分 common 模块：
- `taopiaopiao-common`：无 Web 依赖的核心模块
- `taopiaopiao-common-web`：包含 Web 相关配置和工具

Gateway 只依赖 `taopiaopiao-common`，不依赖 `taopiaopiao-common-web`

### 防范措施
- Gateway 只引入无 Web 依赖的公共模块
- 公共模块需要严格分离 Web 和非 Web 依赖

---

## 2026-02-12 - OpenFeign 解码器循环依赖

### 错误描述
创建 `FeignConfig` 配置类自定义 Decoder，导致启动时报循环依赖错误：
```
Bean 'feignDecoder' defined in FeignDecoderConfig
Circular dependency
```

### 根本原因
自定义 Decoder 注入 `Decoder` 时形成了循环引用

### 解决方案
不创建自定义 Decoder 配置：
1. Client 接口返回 `Result<T>` 类型
2. Service 层通过 `resp.getData()` 获取实体对象
3. 让 Feign 使用默认的 Decoder

### 教训
- OpenFeign 不需要复杂的自定义配置即可正常工作
- 避免在 Decoder 中注入 Decoder 相关 Bean

---

## 2026-02-12 - 更新时间 updatedAt 未自动更新

### 错误描述
更新实体后，数据库中 `updated_at` 字段仍然是旧值，没有更新为当前时间

### 根本原因
MyBatis-Plus 的 `strictUpdateFill` 只在字段为 `null` 时才会填充
从数据库查询出来的实体对象 `updatedAt` 字段已有旧值，不会触发自动填充

### 解决方案
在更新操作前手动设置 `entity.setUpdatedAt(null)`

```java
// 清空 updatedAt，让 MyBatis-Plus 自动填充
existingSession.setUpdatedAt(null);
sessionMapper.updateById(existingSession);
```

### 教训
- 自动填充只在字段为 null 时生效
- 对于更新操作，需要手动清空时间戳字段

---

## 2026-02-12 - 集成 OpenFeign 时的混淆

### 问题
在尝试集成 OpenFeign 时，对于如何处理 `Result<T>` 包装类型产生多次误解：
1. 尝试创建自定义 Decoder → 导致循环依赖
2. 尝试修改其他服务的 Controller 返回类型 → 破坏 API 一致性
3. 混淆了 Client 返回类型和服务返回类型

### 正确理解
1. **Client 接口返回类型**：应与服务实际返回格式一致，即 `Result<VenueResponse>`
2. **Feign 默认 Decoder**：能正确处理 `Result` 类型
3. **Service 层处理**：通过 `resp.getData()` 获取实体对象

### 关键点
- Feign 会自动将服务端返回的 `{"code":200, "data": {...}}` 解析为 `Result` 对象
- `Result.getData()` 即可获取实体对象
- 不需要额外配置

---

## 2026-02-23 - 微服务间错误依赖

### 错误描述
在创建 seat-template-service 时，错误地在 application 模块中直接 import 了其他服务的 DTO：
```java
import com.duanyan.taopiaopiao.venueservice.api.dto.VenueResponse;
```

### 根本原因
违反了微服务边界原则，服务之间不应该直接依赖彼此的 API 模块。

### 解决方案
1. **服务自己的 DTO 定义在自己的 api 模块中**，如 `VenueInfo.java`
2. **跨服务调用通过 Feign Client**，Client 接口定义在调用方服务内部
3. **domain 层完全独立**，只依赖 common 模块

### 教训
- 微服务之间必须通过 Feign/REST API 通信，不能直接依赖彼此的代码
- 每个服务的 api 模块只给外部提供接口定义，不应被其他服务直接 import
- 如果需要其他服务的数据结构，应在本服务内定义轻量级 DTO

---

## 禁止模式清单

1. ❌ 未经用户确认修改 DDL
2. ❌ 假设 AUTO_INCREMENT ID 的值
3. ❌ BeanUtils 直接复制类型不兼容的字段
4. ❌ 在没有配置分页插件的情况下使用 Page 对象
5. ❌ 创建临时测试文件后不清理
6. ❌ Gateway 中引入 Spring MVC 依赖
7. ❌ 创建会导致循环依赖的 Feign Decoder
8. ❌ 跨服务直接 import 其他服务的 DTO

---

## 2026-02-23 - 新增服务后网关路由未配置

### 错误描述
新增 seat-template-service 后，前端请求 `/api/admin/seat-templates` 返回 404

### 根本原因
网关 `application.yml` 中没有配置该服务的路由规则

### 解决方案
在网关配置中添加路由：
```yaml
- id: seat-template-service
  uri: lb://seat-template-service
  predicates:
    - Path=/admin/seat-templates/**, /client/seat-templates/**
  filters:
    - StripPrefix=0
```

### 教训
- **每次新增微服务后，必须在网关配置中添加对应的路由规则**
- 路由 Path 要同时包含管理端和客户端路径
- 使用 `lb://` 协议实现负载均衡

### 防范措施
- 新增服务检查清单中增加"网关路由配置"项
- 建议在项目文档中维护服务列表与网关路由的对应关系

---

## 禁止模式清单

1. ❌ 未经用户确认修改 DDL
2. ❌ 假设 AUTO_INCREMENT ID 的值
3. ❌ BeanUtils 直接复制类型不兼容的字段
4. ❌ 在没有配置分页插件的情况下使用 Page 对象
5. ❌ 创建临时测试文件后不清理
6. ❌ Gateway 中引入 Spring MVC 依赖
7. ❌ 创建会导致循环依赖的 Feign Decoder
8. ❌ 跨服务直接 import 其他服务的 DTO
9. ❌ 新增微服务后忘记配置网关路由

## 最佳实践

1. ✅ 发现不一致时先询问用户
2. ✅ DDL 修改前提供影响分析
3. ✅ 使用 TRUNCATE 清空测试数据
4. ✅ 按依赖顺序导入数据
5. ✅ 及时清理临时文件
