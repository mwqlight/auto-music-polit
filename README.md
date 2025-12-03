# 星韵音域 - 基于SpringBoot+Vue3的全媒介音乐智能驾驶舱

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/spring--boot-3.0+-blue.svg)
![Vue](https://img.shields.io/badge/vue-3.0+-blue.svg)

## 项目概述

"星韵音域"是一个革命性的个人音乐生态系统，将分散在全网的音频资源统一整合为个人专属的智能音乐库。该平台不仅是一个音乐播放器，更是一个能主动采集、智能分析、创造性转化的音乐中枢。

通过先进的爬虫技术从各种开源资源中提取音乐内容，利用AI实现听音识曲、智能编曲等功能，为用户提供从音乐发现、管理到创作的一站式体验。

## 技术架构

### 后端技术栈
- **核心框架**: Spring Boot 3.0+, Spring MVC, Spring Data JPA
- **数据库**: MySQL 8.0+
- **安全框架**: Spring Security + JWT
- **缓存**: Redis
- **API文档**: SpringDoc OpenAPI 3.0
- **构建工具**: Maven

### 前端技术栈
- **核心框架**: Vue 3.3 + TypeScript
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **音频处理**: Howler.js, Tone.js
- **构建工具**: Vite 4
- **UI组件**: Element Plus

### 系统架构图
```
┌─────────────────────────────────────────────────────────────────────┐
│                        星韵音域 - 音乐智能驾驶舱                   │
└───────────────────────┬─────────────────────────────────────────────┘
                        │
┌───────────────────────▼────────────────┐    ┌───────────────────────┐
│           前端应用 (Vue3)             │    │    后端服务 (Spring)  │
│  ┌─────────────────────────────────┐  │    │  ┌─────────────────┐  │
│  │  音乐播放器                     │  │    │  │  音乐管理API    │  │
│  │  (Howler.js/Tone.js)           │  │◄──►│  │  (Music CRUD)   │  │
│  └─────────────────────────────────┘  │    │  └─────────────────┘  │
│  ┌─────────────────────────────────┐  │    │  ┌─────────────────┐  │
│  │  用户界面                       │  │    │  │  用户认证API    │  │
│  │  (Vue Components)              │  │◄──►│  │  (Auth/JWT)     │  │
│  └─────────────────────────────────┘  │    │  └─────────────────┘  │
│  ┌─────────────────────────────────┐  │    │  ┌─────────────────┐  │
│  │  状态管理                       │  │    │  │  播放列表API    │  │
│  │  (Pinia Store)                  │  │◄──►│  │  (Playlist)     │  │
│  └─────────────────────────────────┘  │    │  └─────────────────┘  │
│                                       │    │  ┌─────────────────┐  │
│  ┌─────────────────────────────────┐  │    │  │  收藏管理API    │  │
│  │  HTTP客户端                     │  │◄──►│  │  (Favorites)    │  │
│  │  (Axios)                        │  │    │  └─────────────────┘  │
│  └─────────────────────────────────┘  │    │  ┌─────────────────┐  │
└───────────────────────────────────────┘    │  │  推荐系统API    │  │
                                             │  │  (Recommend)    │  │
┌───────────────────────────────────────┐    │  └─────────────────┘  │
│           数据存储                    │    │  ┌─────────────────┐  │
│  ┌─────────────────────────────────┐  │    │  │  网络爬虫服务   │  │
│  │  MySQL (音乐元数据)             │◄─┼───►│  │  (Web Crawler)  │  │
│  └─────────────────────────────────┘  │    │  └─────────────────┘  │
│  ┌─────────────────────────────────┐  │    │                       │
│  │  Redis (缓存/会话)              │◄─┘    │                       │
│  └─────────────────────────────────┘       └───────────────────────┘
```

## 核心功能模块

### 1. 全域音乐采集中枢
- 多平台音乐资源采集
- 音乐元数据自动补全
- 音频指纹生成与存储

### 2. 听音辩曲大师
- AI音乐识别功能
- 音频片段快速识别
- 相似音乐推荐

### 3. AI音创工坊
- 智能编曲核心API
- 文本到音乐生成功能
- AI音乐混音服务

### 4. 量子音乐库
- 音乐分类与管理
- 智能搜索功能
- 播放列表管理

### 5. 全域播放中枢
- 在线音乐播放
- 播放控制功能
- 收藏与分享

## 项目结构

```
.
├── auto-music-backend/          # 后端服务
│   ├── src/main/java/com/autospacemusic/
│   │   ├── controller/         # REST控制器
│   │   ├── service/            # 业务逻辑
│   │   ├── repository/         # 数据访问层
│   │   ├── entity/             # 实体类
│   │   ├── dto/                # 数据传输对象
│   │   └── config/             # 配置类
│   └── src/main/resources/
│       ├── application.yml     # 配置文件
│       ├── schema.sql          # 数据库结构
│       └── data.sql            # 初始化数据
│
├── auto-music-frontend/        # 前端应用
│   ├── src/
│   │   ├── api/                # API接口封装
│   │   ├── components/         # Vue组件
│   │   ├── views/              # 页面视图
│   │   ├── store/              # 状态管理
│   │   ├── router/             # 路由配置
│   │   └── types/              # TypeScript类型定义
│   ├── public/                 # 静态资源
│   └── vite.config.ts          # 构建配置
│
└── feature-main.md             # 详细功能文档
```

## 快速开始

### 环境要求
- Java 17+
- Node.js 16+
- MySQL 8.0+
- Redis

### 后端启动步骤
1. 配置数据库连接（src/main/resources/application.yml）
2. 运行项目：
   ```bash
   cd auto-music-backend
   mvn spring-boot:run
   ```

### 前端启动步骤
1. 安装依赖：
   ```bash
   cd auto-music-frontend
   npm install
   ```
2. 启动开发服务器：
   ```bash
   npm run dev
   ```

## API文档

启动后端服务后，访问以下地址查看API文档：
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## 数据库设计

系统使用MySQL作为主数据库，包含以下核心表：
- `users`: 用户信息表
- `musics`: 音乐信息表
- `playlists`: 播放列表表
- `playlist_music`: 播放列表与音乐关联表
- `favorites`: 用户收藏表

## 部署说明

### 后端部署
```bash
cd auto-music-backend
mvn clean package
java -jar target/auto-music-backend-0.0.1-SNAPSHOT.jar
```

### 前端部署
```bash
cd auto-music-frontend
npm run build
# 将dist目录部署到Web服务器
```

## 开发规范

项目遵循5A6S开发规范：
- **5A原则**: Architecture(架构)、API(接口)、Automation(自动化)、Assurance(质量保障)、Agility(敏捷协作)
- **6S标准**: Structure(结构)、Standards(标准)、Security(安全)、Stability(稳定)、Scalability(扩展)、Sustainability(可持续)

## 贡献指南

1. Fork 本仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用MIT许可证，详情请见 [LICENSE](LICENSE) 文件。

## 联系方式

项目维护者: [mwqlight](https://github.com/mwqlight)

项目链接: [https://github.com/mwqlight/auto-music-polit](https://github.com/mwqlight/auto-music-polit)