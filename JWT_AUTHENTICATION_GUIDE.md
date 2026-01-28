# JWT认证功能实现说明

## 已实现的功能

### 1. JWT工具类 (`JwtUtil`)
- ✅ 生成Token (包含sub/username/jti/iat/exp)
- ✅ 验证Token
- ✅ 解析Token获取用户信息
- ✅ 检查Token过期
- ✅ 获取Token剩余有效时间

### 2. 登录接口
- **接口**: `POST /api/admin/auth/login`
- **请求**: `{username, password}`
- **响应**: `{token, userInfo}`
- ✅ 用户名密码验证
- ✅ BCrypt密码加密
- ✅ JWT Token生成
- ✅ 用户状态检查

### 3. 登出接口
- **接口**: `POST /api/admin/auth/logout`
- ✅ Token验证
- ⚠️ 黑名单功能(需要Redis,已预留TODO)

### 4. JWT认证过滤器
- ✅ 拦截`/api/admin/*`接口
- ✅ 登录接口放行
- ✅ Token验证
- ✅ 过期检查
- ⚠️ 黑名单检查(需要Redis,已预留TODO)
- ✅ 用户信息注入请求上下文

---

## 前端对接说明

### 1. 登录流程

```javascript
// 前端发送登录请求
POST http://localhost:8080/api/admin/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}

// 成功响应
{
  "code": 0,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "email": "admin@taopiaopiao.com"
    }
  }
}
```

### 2. 保存Token

```javascript
// 前端收到响应后,保存Token
localStorage.setItem('token', response.data.token);

// 或使用Vuex/Pinia管理
store.commit('setToken', response.data.token);
```

### 3. 后续请求携带Token

```javascript
// 方式1: Axios请求拦截器
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }
  return config;
});

// 方式2: 单个请求
axios.get('/api/admin/users', {
  headers: {
    'Authorization': 'Bearer ' + token
  }
});
```

### 4. Nginx配置

前端已配置代理:
```nginx
location /api/ {
    proxy_pass http://localhost:8080/;
}
```

前端请求 `/api/admin/auth/login` → 后端 `http://localhost:8080/api/admin/auth/login`

### 5. 处理401未授权

```javascript
// Axios响应拦截器
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // 清除Token
      localStorage.removeItem('token');
      // 跳转登录页
      router.push('/login');
    }
    return Promise.reject(error);
  }
);
```

---

## Token结构

### JWT Claims
```json
{
  "sub": "1",              // 用户ID
  "username": "admin",    // 用户名
  "jti": "uuid-xxxx",     // Token唯一ID
  "iat": 1706487300,      // 签发时间(秒)
  "exp": 1706573700       // 过期时间(秒)
}
```

### Token有效期
- **默认**: 24小时 (86400秒)
- **配置位置**: `application.yml` → `jwt.expiration`

---

## 数据库准备

### 1. 创建管理员用户

```sql
-- 插入测试管理员用户
-- 密码: password123 (BCrypt加密后的hash)
INSERT INTO admin_users (username, password, real_name, email, status)
VALUES (
  'admin',
  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi',  -- password123
  '系统管理员',
  'admin@taopiaopiao.com',
  'active'
);
```

### 2. 生成BCrypt密码哈希

```java
// 在测试类中生成密码哈希
@Autowired
private PasswordEncoder passwordEncoder;

@Test
public void generatePassword() {
    String rawPassword = "password123";
    String encodedPassword = passwordEncoder.encode(rawPassword);
    System.out.println(encodedPassword);
}
```

---

## 待实现功能

### 1. Token黑名单 (需要Redis)

**优先级**: 中

**实现步骤**:
1. 添加Redis依赖
2. 创建TokenBlacklistService
3. 登出时将JTI加入黑名单
4. 过滤器检查黑名单

**示例代码**:
```java
// 登出时
String jti = jwtUtil.getJti(token);
long ttl = jwtUtil.getRemainingSeconds(token);
redisTemplate.opsForValue().set("blacklist:" + jti, "1", ttl, TimeUnit.SECONDS);

// 过滤器检查
String jti = jwtUtil.getJti(token);
if (Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:" + jti))) {
    sendErrorResponse(response, 401, "令牌已失效");
    return;
}
```

### 2. Token自动刷新

**优先级**: 低

**实现方式**: 双Token机制
- Access Token: 短期有效(15分钟)
- Refresh Token: 长期有效(7天)

---

## 测试

### 1. 使用cURL测试登录

```bash
curl -X POST http://localhost:8080/api/admin/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'
```

### 2. 使用Token访问接口

```bash
curl -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 文件清单

| 文件 | 说明 | 位置 |
|------|------|------|
| JwtConfig.java | JWT配置类 | common/config |
| JwtUtil.java | JWT工具类 | common/util |
| PasswordConfig.java | 密码加密配置 | common/config |
| LoginRequest.java | 登录请求DTO | api/dto |
| LoginResponse.java | 登录响应DTO | api/dto |
| AuthService.java | 认证服务接口 | application/service |
| AuthServiceImpl.java | 认证服务实现 | application/service/impl |
| AuthController.java | 认证控制器 | api/controller |
| JwtAuthenticationFilter.java | JWT过滤器 | api/filter |
| FilterConfig.java | 过滤器配置 | api/config |

---

## 注意事项

1. **密钥安全**: 生产环境必须修改`jwt.secret`
2. **HTTPS**: 生产环境必须使用HTTPS
3. **密码强度**: 建议强制要求强密码
4. **Token存储**: 前端使用localStorage或HttpOnly Cookie
5. **CORS**: 如需跨域,配置CorsFilter
