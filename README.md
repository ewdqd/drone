# 低空研究院平台 (Drone)

低空经济研究院综合管理平台，包含前端展示与后台管理系统。

## 项目结构

```
├── front/                    # 前端项目 (Vue 2)
│   ├── public/               # 静态资源
│   │   ├── favicon.ico
│   │   └── index.html
│   ├── src/
│   │   ├── api/              # API 接口封装
│   │   │   ├── http/         # Axios 封装 / AES 加解密 / 请求配置
│   │   │   └── manage.js     # 管理端接口
│   │   ├── assets/           # 静态资源（图片、字体、CSS）
│   │   │   ├── cover/        # 封面图片
│   │   │   ├── css/          # 全局样式 / 富文本编辑器样式
│   │   │   ├── font-awesome/ # Font Awesome 图标库
│   │   │   ├── fonts/        # 自定义字体
│   │   │   └── images/       # 图片资源
│   │   ├── components/       # 公共组件 (导航栏、富文本编辑器、Tab 等)
│   │   ├── router/           # Vue Router 路由配置
│   │   ├── store/            # Vuex 状态管理
│   │   ├── types/            # TypeScript 类型定义
│   │   ├── utils/            # 工具函数
│   │   └── views/            # 页面组件
│   │       ├── Admin/        # 管理端页面（登录、首页、新闻/产品/用户管理、飞控、轨迹规划等）
│   │       └── Guest/        # 游客端页面（首页、新闻、产品、团队、合作交流、通知、学术等）
│   ├── server.js             # Express 代理服务（抓取就业网新闻）
│   ├── package.json
│   └── vue.config.js
│
├── backen/drone/             # 后端项目 (Spring Boot 3)
│   ├── sql/
│   │   └── xinwen.sql        # 数据库初始化 SQL
│   ├── src/main/java/com/houduan/drone/
│   │   ├── Common/           # 通用类（统一返回结果、自定义 Claims）
│   │   ├── Config/           # 配置类（拦截器、CORS、MyBatis-Plus、Redis、密码加密）
│   │   ├── Context/          # 线程上下文持有者
│   │   ├── Controller/       # 控制器层（无人机排行、登录、新闻、用户管理、操作）
│   │   ├── Dto/              # 数据传输对象
│   │   ├── Entity/           # 数据库实体
│   │   ├── Exception/        # 业务异常
│   │   ├── Mapper/           # MyBatis 数据访问层
│   │   ├── Pojo/             # 枚举 / VO
│   │   ├── Service/          # 业务服务层
│   │   └── Utils/            # 工具类（AES、JWT、路径、Redis 操作）
│   ├── src/main/resources/
│   │   └── application.yml   # 应用配置文件
│   └── pom.xml               # Maven 依赖管理
│
└── README.md                 # 本文件
```

## 技术栈

### 前端
| 技术 | 说明 |
|------|------|
| Vue 2.6 | 前端框架 |
| Vue Router 3 | 路由管理 |
| Vuex 3 | 状态管理 |
| Element UI 2 | UI 组件库 |
| Axios | HTTP 请求 |
| Leaflet | 地图展示 |
| Tailwind CSS (PostCSS7) | 原子化 CSS |
| CryptoJS | AES 加解密 |
| WangEditor | 富文本编辑器 |
| ROSLib | ROS 通信 |

### 后端
| 技术 | 说明 |
|------|------|
| Spring Boot 3.5.7 | 后端框架 |
| Java 17 | 运行环境 |
| MyBatis-Plus 3.5.7 | ORM 框架 |
| MySQL | 关系型数据库 |
| Redis | 缓存 |
| JWT (java-jwt) | 身份认证 |
| Spring Security Crypto | BCrypt 密码加密 |
| Spring GraphQL | GraphQL 支持 |
| Aliyun SMS SDK | 阿里云短信服务 |

## 环境要求

- **Node.js** >= 16
- **Java** >= 17
- **Maven** >= 3.8
- **MySQL** >= 8.0
- **Redis** >= 6.0

## 快速启动

### 1. 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS dec DEFAULT CHARACTER SET utf8mb4;"

# 导入初始数据
mysql -u root -p dec < backen/drone/sql/xinwen.sql
```

### 2. 后端启动

```bash
cd backen/drone

# 修改 application.yml 中的数据库连接信息（用户名、密码等）
# 路径: src/main/resources/application.yml

# 启动（默认端口 9090）
./mvnw spring-boot:run       # macOS / Linux
mvnw.cmd spring-boot:run     # Windows
```

启动后访问：`http://localhost:9090`

### 3. 前端启动

```bash
cd front

# 安装依赖
npm install

# 启动开发服务器（默认端口 8080）
npm run serve

# 可选：启动新闻代理服务（端口 3000）
node server.js
```

启动后访问：`http://localhost:8080`

### 4. 访问页面

| 页面 | 地址 |
|------|------|
| 游客端首页 | `http://localhost:8080/` |
| 管理端登录 | `http://localhost:8080/admin/login` |

## 前端页面一览

### 游客端 (Guest)
- 首页 (`/`) — 研究院首页展示
- 研究院概况 (`/institute-intro`)
- 科研新闻 (`/news-list`, `/news-detail`)
- 通知公告 (`/notice-list`, `/notice-detail`)
- 学术交流 (`/academic-list`, `/academic-detail`)
- 产品展示 (`/product-list`, `/product-detail/:id`)
- 团队风采 (`/team`, `/team-detail/:id`)
- 合作交流 (`/cooperation`)

### 管理端 (Admin)
- 登录 (`/admin/login`)
- 首页 (`/admin/home`)
- 新闻管理 (`/admin/home/news-manage`)
- 用户管理 (`/admin/home/user-manage`)
- 产品管理
- 飞行控制 / 轨迹规划 / 参数设置 等

## 后端 API 概览

| 模块 | Controller | 说明 |
|------|-----------|------|
| 登录认证 | `LoginController` | 用户登录、JWT 签发 |
| 新闻管理 | `NewsController` | 新闻 CRUD |
| 用户管理 | `UserListController` | 用户列表与操作 |
| 无人机排行 | `DroneRankController` | 排名数据 |
| 操作记录 | `ActionOperationController` | 操作日志 |

API 统一前缀：`/api/v1`

## 配置说明

主要配置文件：`backen/drone/src/main/resources/application.yml`

```yaml
server:
  port: 9090                    # 后端服务端口
spring:
  datasource:                   # 数据库连接
  redis:                        # Redis 连接
aes:                            # AES 加密密钥
jwt:                            # JWT 签发配置
aliyun.sms:                     # 阿里云短信（可选）
```

> 生产环境部署前请务必修改数据库密码、JWT secret、AES 密钥等敏感信息。
