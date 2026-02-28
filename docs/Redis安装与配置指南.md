# Redis 安装与配置指南 (Docker 版)

> **环境**: Ubuntu 22.04 LTS 虚拟机 + Docker
> **更新时间**: 2026-02-28

---

## 一、安装 Docker

```bash
# 安装必要依赖
sudo apt update
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

# 添加 Docker 官方 GPG 密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 添加 Docker 仓库
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装 Docker
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# 启动 Docker
sudo systemctl start docker
sudo systemctl enable docker

# 验证安装
sudo docker run hello-world
```

### 免 sudo 使用 Docker（可选）

```bash
sudo groupadd docker
sudo usermod -aG docker $USER
newgrp docker
```

---

## 二、部署 Redis 容器

```bash
# 拉取 Redis 镜像
sudo docker pull redis:7

# 运行 Redis 容器
sudo docker run -d \
  --name taopiaopiao-redis \
  -p 6379:6379 \
  --restart unless-stopped \
  redis:7
```

### 带配置文件运行（可选）

```bash
# 创建配置目录
mkdir -p docker

# 创建配置文件
cat > docker/redis.conf << 'EOF'
bind 0.0.0.0
protected-mode no
appendonly yes
maxmemory 2gb
maxmemory-policy allkeys-lru
EOF

# 运行容器（挂载配置文件）
sudo docker run -d \
  --name taopiaopiao-redis \
  -p 6379:6379 \
  -v $(pwd)/docker/redis.conf:/usr/local/etc/redis/redis.conf \
  --restart unless-stopped \
  redis:7 \
  redis-server /usr/local/etc/redis/redis.conf
```

---

## 三、获取虚拟机 IP

```bash
ip addr show
# 或
hostname -I
```

记下虚拟机的 IP 地址，例如：`192.168.1.100`

---

## 四、验证连接

### 在虚拟机内测试

```bash
sudo docker exec -it taopiaopiao-redis redis-cli
> PING
> PONG
```

### 从 Windows 主机测试

下载 redis-cli：
```
https://github.com/microsoftarchive/redis/releases/download/win-3.0.504/redis-cli.exe
```

连接测试：
```cmd
redis-cli -h 192.168.1.100 -p 6379
> PING
PONG
```

---

## 五、常用命令

```bash
# 查看容器状态
sudo docker ps

# 查看日志
sudo docker logs taopiaopiao-redis

# 停止容器
sudo docker stop taopiaopiao-redis

# 启动容器
sudo docker start taopiaopiao-redis

# 重启容器
sudo docker restart taopiaopiao-redis

# 删除容器
sudo docker rm -f taopiaopiao-redis
```
