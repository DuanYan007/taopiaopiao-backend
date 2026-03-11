# 淘票票后端 - 中间件配置指南

## 一、项目架构概述

本项目采用微服务架构，包含以下服务：

| 服务名称 | 端口 | 功能 |
|---------|------|------|
| gateway | 8080 | API网关 |
| user-service | 8081 | 用户/认证服务 |
| venue-service | 8082 | 场馆服务 |
| event-service | 8083 | 演出服务 |
| session-service | 8084 | 场次服务 |

## 二、必需的中间件

### 1. Nacos (服务注册发现与配置中心)

**下载地址**：https://github.com/alibaba/nacos/releases

**推荐版本**：2.3.0 或更高版本

**安装步骤**：

1. 下载 nacos-server-2.3.0.zip
2. 解压到本地目录（如 `D:\nacos`）
3. 单机模式启动：
   ```bash
   # Windows
   cd D:\nacos\bin
   startup.cmd -m standalone

   # Linux/Mac
   sh startup.sh -m standalone
   ```
4. 访问控制台：http://localhost:8848/nacos
   - 默认用户名：nacos
   - 默认密码：nacos

**配置说明**：

安装完成后，修改各服务的 `application.yml`：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        enabled: true  # 改为 true
      config:
        server-addr: localhost:8848
        enabled: true  # 改为 true
```

### 2. MySQL 数据库

**下载地址**：https://dev.mysql.com/downloads/mysql/

**推荐版本**：MySQL 8.0+ 或 8.4+

**安装步骤**：

1. 下载并安装 MySQL
2. 创建数据库：
   ```sql
   CREATE DATABASE taopiaopiao CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 执行项目中的 SQL 初始化脚本：
   - `sql/init_admin_users.sql`
   - `sql/ddl_sessions.sql`
   - 以及其他 DDL 脚本

**配置说明**：

各服务的 `application.yml` 中已配置数据库连接：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taopiaopiao?...
    username: root
    password: 7566  # 请修改为你的密码
```

### 3. Redis (可选，用于缓存和分布式锁)

**下载地址**：
- Windows：https://github.com/tporadowski/redis/releases
- Linux/Mac：https://redis.io/download

**推荐版本**：Redis 7.0+

**安装步骤**：

1. 下载并解压
2. 启动 Redis：
   ```bash
   # Windows
   redis-server.exe

   # Linux/Mac
   redis-server
   ```

**配置说明**（需要时添加到各服务 pom.xml）：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

```yaml
spring:
  redis:
    host: localhost
    port: 6379
```

### 4. Seata 分布式事务 (可选)

**下载地址**：https://seata.io/zh-cn/blog/download-seata.html

**推荐版本**：2.0.0+

**说明**：在需要强一致性的跨服务事务场景使用。

## 三、启动顺序

1. **启动 MySQL** - 确保数据库服务运行
2. **启动 Nacos** - 单机模式启动
3. **启动各微服务**（任意顺序）：
   ```bash
   # 方式1：IDE 中运行各个启动类
   # 方式2：Maven 打包后运行
   java -jar taopiaopiao-user-service-application.jar
   java -jar taopiaopiao-venue-service-application.jar
   java -jar taopiaopiao-event-service-application.jar
   java -jar taopiaopiao-session-service-application.jar
   ```
4. **启动 Gateway** - 最后启动网关

## 四、验证服务启动

### 1. 检查 Nacos 服务列表

访问 http://localhost:8848/nacos，在"服务管理 -> 服务列表"中查看注册的服务。

### 2. 访问各服务 API

| 服务 | API 地址 | 文档地址 |
|-----|---------|---------|
| user-service | http://localhost:8081/admin/auth/login | http://localhost:8081/doc.html |
| venue-service | http://localhost:8082/admin/venues | http://localhost:8082/doc.html |
| event-service | http://localhost:8083/admin/events | http://localhost:8083/doc.html |
| session-service | http://localhost:8084/admin/sessions | http://localhost:8084/doc.html |

### 3. 测试登录接口

```bash
curl -X POST http://localhost:8081/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

## 五、常见问题

### Q1: Nacos 启动失败

**A**: 检查 Java 版本，需要 JDK 8+。确保端口 8848 未被占用。

### Q2: 服务注册失败

**A**: 确认 Nacos 已启动，检查 `spring.cloud.nacos.server-addr` 配置是否正确。

### Q3: 数据库连接失败

**A**: 检查 MySQL 服务是否启动，用户名密码是否正确。

### Q4: 跨域问题

**A**: Gateway 已配置 CORS，如需自定义请修改 `application.yml` 中的 `globalcors` 配置。

## 六、开发模式快速启动（不使用 Nacos）

在开发阶段，如果不想安装 Nacos，可以直接启动各服务：

1. 各服务配置中 `nacos.discovery.enabled` 和 `nacos.config.enabled` 已默认设为 `false`
2. 直接访问各服务端口即可：
   - user-service: http://localhost:8081
   - venue-service: http://localhost:8082
   - event-service: http://localhost:8083
   - session-service: http://localhost:8084

这种方式下服务间调用会失败，需要等后续添加 Feign Client 后才能正常工作。
