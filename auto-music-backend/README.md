# 星韵音域后端服务

这是"星韵音域"音乐生态系统的后端服务，基于Spring Boot 3.0+构建。

## 项目结构

```
src/main/java/com/autospacemusic/
├── config/         # 配置类
├── controller/     # 控制层（RESTful API）
├── dto/            # 数据传输对象
│   ├── request/    # 请求DTO
│   └── response/   # 响应DTO
├── service/        # 业务逻辑
│   ├── impl/       # 实现类
│   └── Service.java # 接口
├── repository/     # 数据访问层（JPA/Mapper）
├── entity/         # 实体类
├── exception/      # 全局异常处理
└── util/           # 工具类
```

## 功能模块

1. **音乐管理**
   - 音乐上传、检索、更新、删除
   - 音乐搜索功能

2. **用户系统**
   - 用户注册、登录
   - 用户信息管理

3. **播放列表**
   - 创建、管理播放列表
   - 向播放列表添加/删除音乐

4. **收藏功能**
   - 收藏/取消收藏音乐

5. **音乐推荐**
   - 基于用户喜好的个性化推荐
   - 热门音乐推荐

6. **AI音乐服务**
   - 音乐识别
   - AI音乐生成
   - AI音乐混音

7. **网络爬虫**
   - 定时爬取网络音乐资源

## 技术栈

- Spring Boot 3.0+
- Spring Security
- Spring Data JPA
- MySQL
- Redis
- JWT for authentication

## 环境要求

- Java 17+
- MySQL 8.0+
- Redis
- Maven 3.8+

## 快速开始

1. 克隆项目
2. 配置数据库连接（application.yml）
3. 运行项目：
   ```bash
   mvn spring-boot:run
   ```

## API文档

启动项目后，访问 `http://localhost:8080/swagger-ui.html` 查看API文档。

## 配置说明

主要配置项在 `application.yml` 文件中：

- 数据库连接配置
- Redis配置
- JWT密钥和过期时间
- 文件上传路径
- 爬虫定时任务配置

## 数据库设计

数据库表包括：
- users: 用户表
- music: 音乐表
- playlists: 播放列表表
- playlist_music: 播放列表与音乐关联表
- favorites: 收藏表

## 部署说明

1. 打包项目：
   ```bash
   mvn clean package
   ```

2. 运行jar包：
   ```bash
   java -jar target/auto-music-backend-0.0.1-SNAPSHOT.jar
   ```

## 贡献指南

请遵循项目的5A6S开发规范进行开发。

## 许可证

[待定]