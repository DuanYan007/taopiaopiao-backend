# Nacos 配置中心设置指南

## 一、Nacos 配置文件

使用 `optional:nacos:` 前缀表示如果 Nacos 中没有对应配置文件，服务仍然可以正常启动（使用本地配置）。

### 需要在 Nacos 中创建的配置文件

| Data ID | Group | 说明 |
|---------|-------|------|
| `user-service.yml` | DEFAULT_GROUP | 用户服务配置 |
| `venue-service.yml` | DEFAULT_GROUP | 场馆服务配置 |
| `event-service.yml` | DEFAULT_GROUP | 演出服务配置 |
| `session-service.yml` | DEFAULT_GROUP | 场次服务配置 |

### 通用配置模板（复制到各个服务的 Nacos 配置中）

```yaml
# 通用配置示例
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/taopiaopiao?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 7566

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.slf4j.Slf4jImpl
```

## 二、Nacos 控制台操作步骤

1. **登录 Nacos 控制台**
   - 访问：http://localhost:8848/nacos
   - 用户名/密码：nacos/nacos

2. **创建配置**
   - 点击「配置管理」→「配置列表」
   - 点击右上角「+」创建配置
   - 填写以下信息：
     - **Data ID**: `venue-service.yml`（或对应服务名）
     - **Group**: `DEFAULT_GROUP`
     - **配置格式**: `YAML`
     - **配置内容**: 可以先留空或复制上面的通用配置

3. **发布配置**
   - 点击「发布」按钮

## 三、服务配置说明

各服务的配置文件已经在 `application.yml` 中配置了完整的本地配置，包括：
- 数据库连接
- MyBatis-Plus 配置
- 日志配置
- Knife4j 配置

因此，即使 Nacos 配置为空，服务也能正常启动。

## 四、配置优先级

```
Nacos 配置中心配置 > 本地 application.yml 配置
```

如果需要在 Nacos 中覆盖本地配置，只需在 Nacos 配置中添加相同的配置项即可。

## 五、快速验证

启动服务后，可以在 Nacos 控制台的「服务管理」→「服务列表」中看到注册的服务。

如果没有启动 Nacos，服务会因连接失败而报错。此时可以暂时将 `nacos.discovery.enabled` 设为 `false` 来单独测试各服务。
