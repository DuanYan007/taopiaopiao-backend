# 淘票票后端 - 微服务架构说明

## 一、项目结构

```
taopiaopiao-backend/
├── taopiaopiao-common/                      # 公共模块
│   ├── config/                             # 配置类（JWT、密码加密等）
│   ├── constant/                           # 常量定义
│   ├── exception/                          # 异常类
│   ├── response/                           # 统一响应结构
│   └── util/                               # 工具类
│
├── taopiaopiao-gateway/                    # API网关
│   └── src/main/
│       ├── java/.../gateway/
│       │   └── GatewayApplication.java    # 网关启动类
│       └── resources/
│           └── application.yml             # 路由配置
│
├── taopiaopiao-user-service/               # 用户服务
│   ├── taopiaopiao-user-service-api/       # API DTO（供其他服务调用）
│   │   └── dto/
│   │       ├── LoginRequest.java
│   │       ├── LoginResponse.java
│   │       └── UserResponse.java
│   ├── taopiaopiao-user-service-domain/    # 实体和常量
│   │   ├── entity/
│   │   │   └── AdminUser.java
│   │   └── constant/
│   │       └── UserConstants.java
│   └── taopiaopiao-user-service-application/# 启动模块 + Controller + Service + Mapper
│       ├── controller/
│       │   └── AuthController.java
│       ├── service/
│       │   ├── UserService.java
│       │   └── impl/
│       │       └── UserServiceImpl.java
│       ├── mapper/
│       │   └── AdminUserMapper.java
│       ├── UserServiceApplication.java    # 启动类
│       └── resources/
│           ├── application.yml
│           └── mapper/
│               └── AdminUserMapper.xml
│
├── taopiaopiao-venue-service/              # 场馆服务（结构同上）
│   ├── taopiaopiao-venue-service-api/
│   ├── taopiaopiao-venue-service-domain/
│   └── taopiaopiao-venue-service-application/
│
├── taopiaopiao-event-service/              # 演出服务（结构同上）
│   ├── taopiaopiao-event-service-api/
│   ├── taopiaopiao-event-service-domain/
│   └── taopiaopiao-event-service-application/
│
├── taopiaopiao-session-service/            # 场次服务（结构同上）
│   ├── taopiaopiao-session-service-api/
│   ├── taopiaopiao-session-service-domain/
│   └── taopiaopiao-session-service-application/
│
└── 旧版模块（待删除）
    ├── taopiaopiao-api/
    ├── taopiaopiao-application/
    ├── taopiaopiao-domain/
    └── taopiaopiao-infrastructure/
```

## 二、服务划分

| 服务 | 端口 | 职责 | 核心实体 |
|------|------|------|----------|
| **user-service** | 8081 | 用户管理、认证授权 | AdminUser |
| **venue-service** | 8082 | 场馆管理 | Venue |
| **event-service** | 8083 | 演出管理、票档 | Event, TicketTier |
| **session-service** | 8084 | 场次管理、座位 | Session, Seat |
| **gateway** | 8080 | API网关、路由 | - |

## 三、三层架构

每个微服务内部采用简洁的三层架构：

```
Controller (控制器层)
    ↓
Service (业务逻辑层)
    ↓
Mapper (数据访问层 - MyBatis-Plus)
```

### 调用示例

```java
// Controller
@RestController
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    @GetMapping("/{id}")
    public Result<VenueResponse> getById(@PathVariable Long id) {
        return Result.success(venueService.getVenueById(id));
    }
}

// Service
@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {
    private final VenueMapper venueMapper;  // 直接依赖 Mapper

    public VenueResponse getVenueById(Long id) {
        Venue venue = venueMapper.selectById(id);
        // ... 业务逻辑
    }
}

// Mapper
@Mapper
public interface VenueMapper extends BaseMapper<Venue> {
    // MyBatis-Plus 提供基础 CRUD
}
```

## 四、跨服务调用

使用 OpenFeign 实现服务间调用（待实现）：

```java
// event-service 需要调用 venue-service
@FeignClient(name = "venue-service", path = "/admin/venues")
public interface VenueClient {
    @GetMapping("/{id}")
    Result<VenueResponse> getById(@PathVariable("id") Long id);
}
```

## 五、与旧版架构对比

| 维度 | 旧版 DDD 四层 | 微服务三层 |
|------|--------------|-----------|
| **分层** | API/Application/Domain/Infrastructure | Controller/Service/Mapper |
| **依赖** | Service → Repository → Mapper | Service → Mapper |
| **边界** | 按技术层次划分 | 按业务能力划分 |
| **部署** | 单体应用 | 独立部署 |
| **通信** | 内存调用 | HTTP/RPC |

## 六、下一步工作

1. **完善各服务的 CRUD 实现**：将旧版代码迁移到新架构
2. **添加 Feign Client**：实现服务间调用
3. **添加统一认证**：在 Gateway 实现 JWT 验证
4. **添加异常处理**：全局异常处理器
5. **数据分离**：将共享数据库拆分为独立数据库
