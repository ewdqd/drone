# 低空研究院平台 (Drone) — 微服务架构

低空经济研究院综合管理平台，基于 **Apache Dubbo + ZooKeeper** 的微服务架构，集内容管理、用户认证、Redis 缓存、P600 无人机 Web 端远程控制于一体。

## 系统架构

```
┌─────────────────────────────────────────────────────────┐
│                   Frontend (Vue 2 + Element UI)          │
└─────────────────────┬───────────────────────────────────┘
                      │ HTTP (x-auth-token JWT)
┌─────────────────────▼───────────────────────────────────┐
│              drone-gateway (:9090)                       │
│   API 网关 — 统一入口 / CORS / JWT 鉴权                   │
│   通过 Dubbo RPC 调用下游微服务                            │
│   服务发现: ZooKeeper                                     │
└──────┬──────────┬──────────┬──────────┬─────────────────┘
       │ Dubbo    │ Dubbo    │ Dubbo    │ Dubbo
┌──────▼───┐ ┌───▼────┐ ┌───▼────┐ ┌───▼──────┐
│  user    │ │  news  │ │  rank  │ │  action  │
│  :9091   │ │  :9092 │ │  :9093 │ │  :9094   │
│  users   │ │  news  │ │ Redis  │ │  action  │
│  table   │ │ notice │ │ Sorted │ │  table   │
│          │ │ tables │ │  Set   │ │          │
└──────────┘ └────────┘ └────────┘ └──────────┘
       │          │          │          │
       └──────────┴──────────┴──────────┘
                  │ MySQL + Redis
```

## 项目结构

```
├── front/                         # 前端项目 (Vue 2)
│   ├── src/
│   │   ├── api/                   # API 接口 / Axios 封装 / AES 加解密
│   │   ├── assets/                # 静态资源（图片、字体、CSS）
│   │   ├── components/            # 公共组件 (导航栏、富文本编辑器等)
│   │   ├── router/                # Vue Router 路由 + 守卫
│   │   ├── store/                 # Vuex 状态管理
│   │   ├── utils/                 # 工具函数
│   │   └── views/
│   │       ├── Admin/             # 管理端（登录、CMS、飞控、轨迹规划）
│   │       └── Guest/             # 游客端（首页、新闻、团队等 9 页面）
│   ├── server.js                  # Express 代理（就业网新闻抓取）
│   └── package.json
│
├── backen/drone/                  # 后端微服务父工程 (Maven 多模块)
│   ├── pom.xml                    # 父 POM — 依赖管理 + 模块聚合
│   ├── drone-common/              # 共享模块 (JAR)
│   │   └── src/.../drone/
│   │       ├── Common/            # ApiResult, Result, CustomClaim
│   │       ├── Config/            # HttpResultResponse
│   │       ├── Context/           # LocalThreadHolder (ThreadLocal)
│   │       ├── Dto/               # 数据传输对象 (QueryDto, UsersDTO, NewsQueryDto)
│   │       ├── Entity/            # 数据库实体 (Users, News, ActionOperation, Notice)
│   │       ├── Exception/         # BusinessException
│   │       ├── Pojo/              # 枚举 (RoleEnum) / VO (NewsVO, DroneRankVO)
│   │       ├── Utils/             # JwtUtils, AESUtil, RedisOpsUtils, AssertUtils
│   │       └── Service/rpc/       # Dubbo RPC 接口定义
│   │           ├── UserRpcService.java
│   │           ├── NewsRpcService.java
│   │           ├── RankRpcService.java
│   │           └── ActionRpcService.java
│   ├── drone-user-service/        # 用户服务 (:9091)
│   │   ├── Controller/            # LoginController, UserListController
│   │   ├── Service/               # UsersServiceImpl, PageServiceImpl
│   │   ├── Mapper/                # UsersDAO
│   │   ├── Rpc/                   # UserRpcServiceImpl (@DubboService)
│   │   └── Config/                # PasswordEncoderConfig, MybatisPlusConfig
│   ├── drone-news-service/        # 新闻服务 (:9092)
│   │   ├── Service/               # NewsServiceImpl (Redis 缓存旁路)
│   │   ├── Mapper/                # NewsMapper
│   │   ├── Rpc/                   # NewsRpcServiceImpl (@DubboService)
│   │   └── Config/                # RedisConfiguration, MybatisPlusConfig
│   ├── drone-rank-service/        # 排行服务 (:9093)
│   │   ├── Service/               # DroneRankServiceImpl (ZSet 排行榜)
│   │   ├── Rpc/                   # RankRpcServiceImpl (@DubboService)
│   │   └── Config/                # RedisConfiguration
│   ├── drone-action-service/      # 行为服务 (:9094)
│   │   ├── Service/               # ActionOperationServiceImpl
│   │   ├── Mapper/                # ActionOperationMapper
│   │   ├── Rpc/                   # ActionRpcServiceImpl (@DubboService)
│   │   └── Config/                # MybatisPlusConfig
│   └── drone-gateway/             # API 网关 (:9090)
│       ├── Controller/            # GatewayController (统一入口)
│       ├── Config/                # CorsFilter, AuthInterceptor, GlobalMVCConfigurer
│       └── (通过 @DubboReference 调用下游服务)
│
└── README.md
```

## 技术栈

### 前端
| 技术 | 说明 |
|------|------|
| Vue 2.6 + Vue Router 3 + Vuex 3 | 前端框架 |
| Element UI 2 + Tailwind CSS | UI 组件库 + 原子化 CSS |
| Axios + CryptoJS | HTTP 请求 + AES 加解密 |
| ROSLIB.js | ROS Bridge WebSocket 通信 |
| Leaflet + 高德地图 | 地图展示 + 轨迹规划 |
| WangEditor | 富文本编辑器 |

### 后端微服务
| 技术 | 说明 |
|------|------|
| Spring Boot 3.5.7 + Java 17 | 应用框架 |
| Apache Dubbo 3.3 + ZooKeeper 3.8 | RPC + 服务注册发现 |
| MyBatis-Plus 3.5.7 + MySQL | ORM + 关系型数据库 |
| Redis (Lettuce) | 缓存 / ZSet 排行榜 |
| JWT (auth0 java-jwt) + BCrypt | 无状态认证 + 密码加密 |
| Spring GraphQL + Swagger | API 查询 + 文档 |

## 微服务架构设计

### 为什么选择 Dubbo + ZooKeeper？

1. **高性能 RPC**：Dubbo 默认 Hessian2 序列化 + Netty NIO 传输，比 HTTP REST 更适合内部微服务高频调用
2. **服务治理**：内置负载均衡（Random/RoundRobin/LeastActive/ConsistentHash）、容错（Failover/Failfast）、熔断降级
3. **ZooKeeper 注册中心**：CP 模型保证服务列表强一致性，临时节点实现服务自动摘除，Watch 机制实时感知上下线
4. **注册中心与服务调用解耦**：ZooKeeper 宕机不影响已有服务调用（本地缓存 Provider 列表）

### 服务拆分原则

| 服务 | 端口 | 数据库表 | 职责 |
|------|------|---------|------|
| **gateway** | 9090 | 无 | 统一入口 / JWT 鉴权 / CORS / Dubbo Consumer |
| **user-service** | 9091 | users | 登录认证 / 用户 CRUD / 分页查询 |
| **news-service** | 9092 | news, notice | 新闻 CRUD / 缓存旁路 / 穿透击穿雪崩防护 |
| **rank-service** | 9093 | 无 (Redis) | ZSet 排行 / 飞行时长统计 |
| **action-service** | 9094 | action | 用户行为记录（点赞/阅读/收藏） |

### 认证流程（跨服务 JWT）

```
Client → Gateway (JWT 验签 + ThreadLocal) → Dubbo RPC → 下游服务
  1. POST /api/v1/login → Gateway → Dubbo → user-service 验证密码
  2. user-service 返回用户信息 → Gateway 签发 JWT → 返回客户端
  3. 后续请求 Header 带 x-auth-token → Gateway AuthInterceptor 验签
  4. 验签通过 → Gateway 通过 @DubboReference 调用下游服务
```

## 环境要求

- **Node.js** >= 16
- **Java** >= 17
- **Maven** >= 3.8
- **MySQL** >= 8.0
- **Redis** >= 6.0
- **ZooKeeper** >= 3.8

## 快速启动

### 1. 基础设施

```bash
# 启动 MySQL、Redis、ZooKeeper
# ZooKeeper (Docker)
docker run -d -p 2181:2181 zookeeper:3.8

# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS dec DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p dec < backen/drone/sql/xinwen.sql
```

### 2. 后端微服务

```bash
cd backen/drone

# 编译所有模块
mvn clean install -DskipTests

# 按顺序启动各服务（每个终端窗口一个）
mvn -pl drone-user-service spring-boot:run     # :9091
mvn -pl drone-news-service spring-boot:run     # :9092
mvn -pl drone-rank-service spring-boot:run     # :9093
mvn -pl drone-action-service spring-boot:run   # :9094
mvn -pl drone-gateway spring-boot:run          # :9090
```

### 3. 前端

```bash
cd front
npm install
npm run serve          # :8080
```

### 4. 访问

| 页面 | 地址 |
|------|------|
| 游客端首页 | `http://localhost:8080/` |
| 管理端登录 | `http://localhost:8080/admin/login` |

## Dubbo 服务管理

```bash
# 查看 ZooKeeper 中注册的 Dubbo 服务
zkCli.sh -server localhost:2181
ls /dubbo
ls /dubbo/com.houduan.drone.Service.rpc.UserRpcService/providers

# Dubbo Admin 可视化面板（可选）
docker run -d -p 8081:8080 apache/dubbo-admin
```

## 配置说明

每个服务有独立的 `application.yml`，关键配置：

```yaml
# Dubbo 通用配置
dubbo:
  application:
    name: drone-xxx-service
  registry:
    address: zookeeper://localhost:2181
  protocol:
    name: dubbo
    port: -1          # 自动分配端口
  provider:
    timeout: 5000

# Gateway 额外配置
dubbo:
  consumer:
    check: false       # 启动时不检查 Provider
```

> 生产环境必须修改：数据库密码、JWT secret、ZooKeeper 地址、Redis 密码等敏感信息。
