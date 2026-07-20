# Drone — 新闻管理与无人机排行榜微服务系统

基于 Spring Boot 3 + Dubbo + MyBatis-Plus + Redis 的微服务架构系统，
提供用户管理、新闻管理、用户行为追踪和无人机飞行时长排行榜功能。

## 技术栈

| 组件 | 技术 |
|------|------|
| 框架 | Spring Boot 3.5.7 |
| RPC | Apache Dubbo 3.3.4 |
| 注册中心 | ZooKeeper |
| ORM | MyBatis-Plus 3.5.7 |
| 缓存 | Redis (Lettuce) |
| 认证 | JWT (Auth0 java-jwt) + BCrypt |
| 数据库 | MySQL 8 |
| 构建 | Maven |
| Java | 17 |

## 项目结构

```
drone/
├── drone-common/           # 公共模块（实体、DTO、工具类、RPC接口）
├── drone-gateway/          # API 网关（统一入口、JWT认证、请求路由）
├── drone-user-service/     # 用户服务（注册、登录、CRUD）
├── drone-news-service/     # 新闻服务（CRUD、缓存旁路、防穿透/击穿/雪崩）
├── drone-rank-service/     # 排行榜服务（Redis ZSet 飞行时长排名）
├── drone-action-service/   # 行为追踪服务（点赞/阅读/收藏/停留记录）
└── sql/                    # 数据库初始化脚本
```

## 快速启动

### 1. 环境准备

- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- ZooKeeper 3.8+

### 2. 数据库初始化

```bash
mysql -u root -p < sql/xinwen.sql
```

### 3. 配置环境变量

```bash
# 数据库
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=dec
export DB_USERNAME=root
export DB_PASSWORD=your-password

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=

# JWT（生产环境请使用强密钥）
export JWT_SECRET=your-strong-jwt-secret-at-least-32-characters
```

### 4. 启动服务

按顺序启动（需要先启动 ZooKeeper）：

```bash
# 1. 用户服务 (port 9091)
cd drone-user-service && mvn spring-boot:run

# 2. 新闻服务 (port 9092)
cd drone-news-service && mvn spring-boot:run

# 3. 排行榜服务 (port 9093)
cd drone-rank-service && mvn spring-boot:run

# 4. 行为追踪服务 (port 9094)
cd drone-action-service && mvn spring-boot:run

# 5. API 网关 (port 9090)
cd drone-gateway && mvn spring-boot:run
```

## API 端点

### 认证
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/manage/api/v1/login` | 用户登录 |
| POST | `/manage/api/v1/token/refresh` | 刷新 Token |

### 用户管理（需认证）
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/user/updateUser` | 分页查询用户 |
| POST | `/user/adduser` | 注册用户 |
| POST | `/user/userModify` | 修改用户 |
| POST | `/user/userDelete` | 删除用户 |

### 新闻管理
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/news/saveNewsEntity` | 新增新闻（管理员） |
| PUT | `/news/updateNewsEntity` | 更新新闻（管理员） |
| DELETE | `/news/{id}` | 删除新闻（管理员） |
| GET | `/news/{id}` | 查询新闻详情（Redis缓存） |
| POST | `/news/list` | 分页列表 |
| POST | `/news/setShowStatus` | 设置显示状态 |

### 排行榜
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/manage/api/v1/drone/flight/add` | 增加飞行时长 |
| GET | `/manage/api/v1/drone/ranking/top` | Top-N 排行 |

### 行为追踪
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/action-operation/saveActionEntity` | 记录用户行为 |

## 架构亮点

### 缓存策略（防穿透、防击穿、防雪崩）
- **穿透防护**：缓存空对象 60 秒
- **击穿防护**：分布式锁（Redis SETNX）
- **雪崩防护**：过期时间 + 随机值（1800 + random 0~299 秒）
- **启动预热**：@PostConstruct 加载热门新闻到 Redis

### 安全机制
- JWT 认证 + BCrypt 密码加密
- 请求拦截器验证所有请求（login/refresh 除外）
- ThreadLocal 用户上下文 + 请求完成后自动清理
- 角色权限校验（管理员/普通用户）

## 最近修复 (2026-07)

- ✅ 删除 AES 加密功能（由 HTTPS 传输层保障）
- ✅ 修复 `userDelete` 接口参数接收方式
- ✅ 修复 `LocalThreadHolder` NPE 风险
- ✅ 修复 `NewsServiceImpl` 新闻不存在时的空值处理
- ✅ 添加用户注册用户名重复检查
- ✅ 添加用户修改/删除的存在性校验
- ✅ 恢复 `ActionOperation` 用户关联
- ✅ 数据库密码改为环境变量注入
- ✅ JWT 密钥改为环境变量注入
- ✅ 修复 URL 拼写错误 `/updatauser` → `/updateUser`
- ✅ 添加 `@Transactional` 事务保护
- ✅ JWT 过期时间修复（666秒 → 86400秒 = 24小时）
- ✅ 移除 `System.out.println` 调试输出，改用 Slf4j 日志
