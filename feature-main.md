# 星韵音域 - 基于SpringBoot+Vue3+Uniapp的全媒介音乐智能驾驶舱

## 项目概述

打造一个革命性的个人音乐生态系统，将分散在全网的音频资源统一整合为个人专属的智能音乐库。"星韵音域"不仅是一个音乐播放器，更是一个能主动采集、智能分析、创造性转化的音乐中枢。该平台通过先进的爬虫技术从各种开源资源中提取音乐内容，利用AI实现听音识曲、智能编曲等功能，为用户提供从音乐发现、管理到创作的一站式体验。系统设计遵循"纯粹音乐收集盒"理念，界面采用高科技视觉风格，确保在PC端和移动端都能提供一致且沉浸的音乐体验，同时严格遵守各平台内容政策，尊重版权，仅处理用户授权或开源许可的音乐资源。

## 技术栈要求
- **后端核心**：Spring Boot 3.2 + Spring AI + WebFlux(异步处理) + Quartz(定时任务)
- **爬虫引擎**：Jsoup + Selenium(动态页面) + Apache Tika(内容分析) + FFmpeg(音视频转换)
- **AI组件**：Librosa(音频分析) + TensorFlow/PyTorch(音乐识别) + MusicTransformer(智能编曲)
- **数据存储**：PostgreSQL(元数据) + MinIO(音频文件) + Redis(缓存/会话) + Elasticsearch(搜索)
- **前端框架**：Vue3 + TypeScript + Pinia + Vite 5
- **跨端方案**：Uniapp + Vue3组合式API + UniPush(消息推送)
- **音频处理**：Howler.js(播放) + Web Audio API(高级处理) + Tone.js(合成/编曲)
- **部署架构**：Docker + Nginx(静态资源) + Prometheus(监控) + FFmpeg容器(转码服务)

## 系统架构设计
```
┌───────────────────────────────────────────────────────────────────────────────────┐
│                            星韵音域 - 音乐智能驾驶舱                             │
│                (量子视觉界面 + 3D音频波形 + 智能推荐中枢 + 跨端同步)              │
└───────────────────────┬───────────────────────────────────────┬───────────────────┘
                        │                                       │
┌───────────────────────▼───────┐       ┌───────────────────────▼───────────────────┐
│     前端应用层                │       │        后端服务层                         │
│  ┌─────────────┐  ┌──────────┐│       │  ┌─────────────────────────────────────┐  │
│  │  Web应用    │  │  Uniapp  ││       │  │  API Gateway (路由/认证/限流)        │  │
│  │ (Vue3+TS)   │  │ (跨端APP)││       │  └───────────────────┬─────────────────┘  │
│  └─────────────┘  └──────────┘│       │                      │                    │
│        │               │      │       │  ┌──────────────────▼─────────────────┐  │
│        └───────┬───────┘      │       │  │        微服务集群                  │  │
│                │              │       │  │  ┌─────────────┐  ┌──────────────┐  │  │
│  ┌─────────────▼────────────┐ │       │  │  │音乐采集服务  │  │ 音频处理服务 │  │  │
│  │   统一音频渲染引擎       │ │       │  │  │(爬虫引擎)   │  │(转码/增强)   │  │  │
│  │  (Web Audio API+Tone.js) │ │◄─────►│  │  └─────────────┘  └──────────────┘  │  │
│  └──────────────────────────┘ │       │  │  ┌─────────────┐  ┌──────────────┐  │  │
│                │              │       │  │  │AI音乐中枢   │  │用户服务      │  │  │
│  ┌─────────────▼────────────┐ │       │  │  │(听音辩曲/   │  │(收藏/播放/   │  │  │
│  │   量子视觉交互系统       │ │       │  │  │ 智能编曲)   │  │  数据分析)   │  │  │
│  │ (3D波形+粒子效果+主题)   │ │       │  │  └─────────────┘  └──────────────┘  │  │
│  └──────────────────────────┘ │       │  └───────────────────┬─────────────────┘  │
└───────────────────────────────┘       │                      │                    │
                                        │  ┌──────────────────▼─────────────────┐  │
                                        │  │        数据层                      │  │
                                        │  │  ┌─────────────┐  ┌──────────────┐  │  │
                                        │  │  │ PostgreSQL  │  │   MinIO      │  │  │
                                        │  │  │(元数据/用户)│  │(音频文件存储)│  │  │
                                        │  │  └─────────────┘  └──────────────┘  │  │
                                        │  │  ┌─────────────┐  ┌──────────────┐  │  │
                                        │  │  │  Redis      │  │Elasticsearch │  │  │
                                        │  │  │(会话/缓存)  │  │(全文搜索)    │  │  │
                                        │  │  └─────────────┘  └──────────────┘  │  │
                                        │  └─────────────────────────────────────┘  │
                                        └───────────────────────────────────────────┘
```

## 核心功能模块

### 1. 全域音乐采集中枢
#### **智能爬虫引擎**
- **多源采集能力**：
  - 视频平台提取（YouTube/B站/抖音等开源内容）
  - 音频分享网站（SoundCloud/Free Music Archive等）
  - 开源音乐库（Musopen/IMSLP等古典音乐资源）
  - 个人网盘/云存储（通过授权API接入）
  - 网页音频元素自动识别（HTML5 audio标签）
- **智能内容识别**：
  - 音频质量自动评估（采样率/位深度/信噪比）
  - 版权状态识别（CC协议/开源许可/公共领域）
  - 音频内容分类（人声/纯音乐/环境音）
  - 情绪/风格自动标记（欢快/悲伤/放松/专注）
- **自适应抓取策略**：
  - 增量更新（仅抓取新内容）
  - 智能速率控制（避免IP封禁）
  - 代理轮换系统（分布式爬取）
  - 失败重试与异常处理

#### **资源转换工厂**
- **全方位格式支持**：
  - 视频到音频转换（MP4/WebM→MP3/FLAC/WAV）
  - 低质量增强（AI降噪/采样率提升）
  - 分段提取（从长视频中提取BGM/片段）
  - 多声道处理（立体声/5.1环绕声）
- **元数据丰富化**：
  - 自动封面获取/生成
  - 艺术家/专辑信息补全
  - 歌词自动匹配
  - BPM(节拍)自动检测
  - 音调/调性识别

### 2. 量子音乐库
#### **智能组织系统**
- **多维分类体系**：
  - 传统分类（艺术家/专辑/流派/年份）
  - 情感分类（情绪/场景/活动）
  - 技术分类（BPM/调性/乐器/音质）
  - 自定义标签（颜色/季节/记忆）
- **动态分组**：
  - 智能播放列表（基于行为/偏好）
  - 时空分组（特定地点/时间收听的音乐）
  - 情绪曲线分组（从平静到激昂）
  - 社交分组（与特定人的共享音乐）

#### **极致搜索体验**
- **多模态搜索**：
  - 文本搜索（标题/艺术家/歌词）
  - 语音搜索（哼唱/口哨识别）
  - 情绪搜索（"找一首让我放松的音乐"）
  - 场景搜索（"适合工作专注的背景音乐"）
- **高级筛选**：
  - 音频特征筛选（BPM范围/调性/音质）
  - 时长/年代/来源筛选
  - 播放历史/频率筛选
  - 相似音乐推荐（基于音频特征）

### 3. 听音辩曲大师
#### **音频指纹识别**
- **实时识别能力**：
  - 环境噪音过滤
  - 片段识别（3-5秒足够）
  - 多版本识别（不同演奏/翻唱）
  - 混音/变调识别
- **离线识别**：
  - 本地音频库指纹数据库
  - 低带宽优化（无需实时联网）
  - 历史记录关联（识别过的歌曲自动入库）
  - 未知歌曲提交（社区协作识别）

#### **音乐DNA分析**
- **深度特征提取**：
  - 旋律轮廓提取
  - 和声进行分析
  - 节奏模式识别
  - 音色特征分解
- **智能关联**：
  - 相似艺术家推荐
  - 影响关系图谱（谁影响了谁）
  - 风格演化追踪
  - 跨文化音乐关联

### 4. 量子音创工坊
#### **AI智能编曲**
- **文本到音乐**：
  - 自然语言描述转音乐（"欢快的夏日海滩，轻快的萨克斯风，慢节奏"）
  - 曲风融合（"将古典钢琴与电子节拍融合"）
  - 情绪驱动（"表达思念之情的抒情旋律"）
  - 场景定制（"适合冥想的环境音乐，带有流水声"）
- **创作辅助**：
  - 和弦建议（基于当前旋律）
  - 旋律补全（AI续写未完成乐句）
  - 音色推荐（匹配情绪/风格的乐器组合）
  - 编曲层次建议（主旋律/和声/节奏层）

#### **个性化混音**
- **实时混音**：
  - 节拍同步混音（无缝过渡）
  - 情绪曲线混音（渐进式情绪变化）
  - 3D空间混音（声音位置定位）
  - 动态均衡调整（适配不同设备）
- **音效增强**：
  - 空间音效（虚拟环绕声）
  - 节拍可视化同步
  - 动态低音增强
  - 人声分离/突出

### 5. 全域播放中枢
#### **无缝播放体验**
- **后台播放**：
  - Android/iOS后台持续播放
  - 锁屏控制界面
  - 通知栏快捷控制
  - 耳机/蓝牙设备控制
- **跨设备同步**：
  - 播放进度云同步
  - 设备间无缝切换（手机→PC→平板）
  - 多房间音频同步（家庭音响系统）
  - 低延迟技术（<100ms）

#### **沉浸式播放**
- **3D音频波形**：
  - 实时频谱可视化（3D粒子效果）
  - 音频特征动态展示（BPM脉冲/音调变化）
  - 情绪色彩映射（音乐情绪→颜色变化）
  - 交互式波形探索（点击跳转/缩放）
- **场景化播放**：
  - 动态背景（基于音乐情绪变化）
  - 粒子动画（响应音乐节奏）
  - 光效同步（设备LED/智能家居联动）
  - 触觉反馈（支持触觉反馈设备）

### 6. 音域排行榜
#### **智能统计系统**
- **个人播放数据**：
  - 每日/每周/每月收听时长
  - 情绪偏好变化趋势
  - 收听时段分析
  - 艺术家/流派偏好演变
- **社区榜单**：
  - 好友圈热门音乐
  - 相似用户推荐榜
  - 新发现音乐热度榜
  - 自定义挑战榜（"最放松的周末"）

#### **洞察与推荐**
- **音乐发现**：
  - 被忽视的珍宝（高质量但少播放）
  - 记忆关联推荐（"上次在咖啡馆听的音乐"）
  - 季节性推荐（基于天气/季节）
  - 潮流预测（上升中的新艺术家）
- **成就系统**：
  - 收听里程成就
  - 音乐探索成就（发现新流派/艺术家）
  - 创作成就（完成编曲/混音）
  - 社交成就（分享/协作）

## UI/UX设计规范
### 1. 量子声学视觉系统
- **核心视觉语言**：
  - 主色系：深空黑(#0d0a1f) + 星云紫(#9d50ff) + 量子蓝(#00e0ff) + 能量橙(#ff6b35)
  - 动态背景：粒子流 + 音频波纹 + 深度渐变
  - 元素风格：半透明磨砂玻璃 + 发光边框 + 悬浮阴影 + 3D空间深度
- **关键设计元素**：
  - 音频粒子：随音乐跳动的发光粒子
  - 波形隧道：3D音频波形通道，可"穿行"
  - 光流连接：UI元素间流动的数据光线
  - 情绪光谱：音乐情绪映射到背景色彩
  - 全息控件：悬浮式播放/音量控制
- **交互动效原则**：
  - 元素入场：声波扩散效果
  - 状态转换：音频波纹过渡
  - 按钮反馈：音符涟漪
  - 加载状态：粒子聚合动画
  - 选择效果：能量环绕

### 2. 驾驶舱主界面布局

#### **PC端布局**
```
┌─────────────────────────────────────────────────────────────────────────────────┐
│ 量子顶栏：系统Logo + 全局搜索/语音 + 播放控制 + 通知 + 用户中心                │
├─────────────────────────────────────────────────────────────────────────────────┤
│ 沉浸式主视图区：3D音频波形 + 实时频谱 + 情绪色彩映射 + 交互式音乐宇宙          │
├───────────────────┬───────────────────────────────────┬─────────────────────────┤
│ 音乐导航面板      │ 当前播放/推荐中心                │ AI音创工坊             │
│ - 智能分类        │ - 正在播放详情                   │ - 文本到音乐           │
│ - 动态播放列表    │ - 智能推荐(基于当前情绪)         │ - 曲风分析器           │
│ - 最近添加        │ - 情绪/场景推荐                  │ - 旋律生成器           │
│ - 收藏集锦        │ - 社区热门                       │ - 音色实验室           │
├───────────────────┼───────────────────────────────────┼─────────────────────────┤
│ 音源采集控制台    │ 数据洞察中心                     │ 系统设置               │
│ - 爬虫任务监控    │ - 个人音乐画像                   │ - 量子视觉设置         │
│ - 源站配置        │ - 收听习惯分析                   │ - 音频增强选项         │
│ - 质量过滤器      │ - 情绪变化趋势                   │ - 跨设备同步设置       │
│ - 增量更新        │ - 成就/里程碑                    │ - 隐私/数据管理        │
└───────────────────┴───────────────────────────────────┴─────────────────────────┘
```

#### **移动端布局 (Uniapp)**
```
┌─────────────────────────────────────────────────┐
│ 量子状态栏：时间 + 电池 + 信号 + 播放状态指示器 │
├─────────────────────────────────────────────────┤
│ 沉浸式主视图：全屏3D音频波形 + 动态情绪背景     │
│                 (支持手势交互)                  │
├─────────────────────────────────────────────────┤
│ 底部导航：                                      │
│  [宇宙]  [发现]  [AI+]  [我的]  [采集]         │
│  (3D悬浮按钮 + 微动效反馈)                      │
├─────────────────────────────────────────────────┤
│ 情境控制面板 (从底部上滑)：                     │
│  - 精细播放控制 (进度/音量/均衡)                │
│  - 场景模式切换 (专注/放松/活力)                │
│  - 音效增强选项 (空间/低音/清晰度)              │
└─────────────────────────────────────────────────┘
```

### 3. 核心交互模式
#### **多模态控制**
- **手势交互**：
  - 双指旋转：3D波形视角控制
  - 三指滑动：在不同音乐宇宙间切换
  - 画圈手势：创建自定义播放列表
  - 捏合缩放：深入音频频谱细节
- **语音控制**：
  - "播放我收藏的放松音乐"
  - "识别这首歌"
  - "创建一个欢快的夏日播放列表"
  - "将这段旋律转成古典钢琴版本"
- **情境感知**：
  - 基于时间/位置自动切换播放模式
  - 基于设备姿态调整界面(横屏/竖屏)
  - 基于环境噪音调整播放策略
  - 基于用户情绪(通过摄像头/可穿戴设备)调整推荐

## 技术实现细节

### 1. 爬虫服务核心实现
```java
@Service
public class MusicCrawlerService {
    
    @Value("${crawler.user-agent}")
    private String userAgent;
    
    @Value("${crawler.proxy-rotation.enabled}")
    private boolean proxyRotationEnabled;
    
    @Autowired
    private AudioExtractionService audioExtractionService;
    
    @Autowired
    private MusicMetadataService metadataService;
    
    @Autowired
    private ContentFilterService contentFilterService;
    
    // 支持的源站配置
    private Map<String, SiteConfig> siteConfigs = new HashMap<>();
    
    @PostConstruct
    public void initSiteConfigs() {
        // 初始化各平台爬取规则
        siteConfigs.put("youtube", new SiteConfig(
            "https://www.youtube.com/results?search_query={keyword}",
            "div#contents ytd-video-renderer",
            "a#video-title", 
            data -> extractYouTubeMetadata(data)
        ));
        
        siteConfigs.put("bilibili", new SiteConfig(
            "https://search.bilibili.com/all?keyword={keyword}",
            "div.video-list div.video-item",
            "a.title", 
            data -> extractBilibiliMetadata(data)
        ));
        
        // 其他源站配置...
    }
    
    /**
     * 智能爬取音乐资源
     */
    public CrawlResult crawlMusicResources(CrawlRequest request) {
        // 验证请求合法性
        if (!contentFilterService.validateRequest(request)) {
            throw new SecurityException("请求包含不安全内容");
        }
        
        List<MusicResource> resources = new ArrayList<>();
        List<CrawlError> errors = new ArrayList<>();
        
        // 并行爬取多个源
        List<CompletableFuture<Void>> futures = request.getSources().stream()
            .map(source -> CompletableFuture.runAsync(() -> {
                try {
                    SiteConfig config = siteConfigs.get(source);
                    if (config == null) {
                        throw new IllegalArgumentException("不支持的源站: " + source);
                    }
                    
                    // 构建搜索URL
                    String searchUrl = config.getSearchUrl()
                        .replace("{keyword}", URLEncoder.encode(request.getKeyword(), StandardCharsets.UTF_8));
                    
                    // 获取代理(如启用)
                    ProxyConfig proxy = proxyRotationEnabled ? 
                        proxyService.getNextProxy() : null;
                    
                    // 执行爬取
                    Document doc = Jsoup.connect(searchUrl)
                        .userAgent(userAgent)
                        .proxy(proxy != null ? proxy.getHost() : null, 
                               proxy != null ? proxy.getPort() : 0)
                        .timeout(10000)
                        .get();
                    
                    // 提取资源
                    Elements items = doc.select(config.getItemSelector());
                    for (Element item : items) {
                        try {
                            Element link = item.selectFirst(config.getLinkSelector());
                            if (link != null) {
                                String url = link.attr("href");
                                if (!url.startsWith("http")) {
                                    url = "https://" + source + ".com" + url;
                                }
                                
                                // 获取详情页
                                Document detailDoc = Jsoup.connect(url)
                                    .userAgent(userAgent)
                                    .timeout(15000)
                                    .get();
                                
                                // 提取元数据
                                MusicMetadata metadata = config.getMetadataExtractor().extract(detailDoc);
                                
                                // 检查版权状态
                                if (contentFilterService.checkCopyrightStatus(metadata)) {
                                    // 提取音频
                                    AudioExtractionResult audioResult = audioExtractionService.extractAudio(url, metadata);
                                    
                                    if (audioResult.isSuccess()) {
                                        MusicResource resource = new MusicResource();
                                        resource.setUrl(url);
                                        resource.setSource(source);
                                        resource.setAudioUrl(audioResult.getAudioUrl());
                                        resource.setMetadata(metadata);
                                        resource.setAudioQuality(audioResult.getQualityScore());
                                        resource.setCopyrightStatus(audioResult.getCopyrightStatus());
                                        resources.add(resource);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            errors.add(new CrawlError(source, url, e.getMessage()));
                        }
                    }
                } catch (Exception e) {
                    errors.add(new CrawlError(source, searchUrl, e.getMessage()));
                }
            }, executorService))
            .collect(Collectors.toList());
        
        // 等待所有爬取任务完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        
        // 保存结果到数据库
        if (!resources.isEmpty()) {
            musicRepository.saveAll(resources);
        }
        
        return new CrawlResult(resources, errors);
    }
    
    /**
     * 增量更新已有资源
     */
    @Scheduled(cron = "${crawler.incremental-update.cron}")
    public void performIncrementalUpdate() {
        // 1. 获取上次更新时间戳
        // 2. 查询新内容
        // 3. 智能过滤已处理内容
        // 4. 仅处理新增内容
    }
}
```

### 2. AI音乐识别核心实现 (Python服务通过REST API集成)
```python
from fastapi import FastAPI, UploadFile, File
from pydub import AudioSegment
import librosa
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model
import os
import io

app = FastAPI()

# 加载预训练模型
music_recognition_model = load_model('models/music_recognition_v3.h5')
embedding_db = load_embedding_database('data/music_embeddings_v2.npy')

def extract_audio_features(audio_data, sample_rate=22050):
    """提取音频特征向量"""
    # 提取梅尔频谱
    mel_spec = librosa.feature.melspectrogram(y=audio_data, sr=sample_rate, n_mels=128)
    mel_spec_db = librosa.power_to_db(mel_spec, ref=np.max)
    
    # 提取MFCC
    mfccs = librosa.feature.mfcc(y=audio_data, sr=sample_rate, n_mfcc=20)
    
    # 提取节奏特征
    tempo, beat_frames = librosa.beat.beat_track(y=audio_data, sr=sample_rate)
    
    # 合并特征
    features = {
        'mel_spectrogram': mel_spec_db,
        'mfccs': mfccs,
        'tempo': tempo,
        'spectral_centroid': librosa.feature.spectral_centroid(y=audio_data, sr=sample_rate),
        'spectral_rolloff': librosa.feature.spectral_rolloff(y=audio_data, sr=sample_rate),
        'zero_crossing_rate': librosa.feature.zero_crossing_rate(audio_data)
    }
    
    return features

def generate_audio_embedding(features):
    """生成音频嵌入向量"""
    # 将特征转换为模型输入
    input_tensor = preprocess_features_for_model(features)
    
    # 生成嵌入
    embedding = music_recognition_model.predict(input_tensor)
    
    return embedding

def find_similar_tracks(embedding, top_k=5):
    """在数据库中查找相似曲目"""
    # 计算余弦相似度
    similarities = cosine_similarity(embedding, embedding_db['embeddings'])
    
    # 获取最相似的top_k结果
    top_indices = np.argsort(similarities)[::-1][:top_k]
    results = []
    
    for idx in top_indices:
        track = {
            'id': embedding_db['track_ids'][idx],
            'title': embedding_db['titles'][idx],
            'artist': embedding_db['artists'][idx],
            'album': embedding_db['albums'][idx],
            'similarity': float(similarities[idx]),
            'audio_url': embedding_db['audio_urls'][idx],
            'cover_url': embedding_db['cover_urls'][idx]
        }
        results.append(track)
    
    return results

@app.post("/api/v1/audio/identify")
async def identify_audio(file: UploadFile = File(...)):
    """识别上传的音频片段"""
    try:
        # 读取音频文件
        contents = await file.read()
        
        # 转换为标准格式
        audio = AudioSegment.from_file(io.BytesIO(contents))
        audio = audio.set_channels(1).set_frame_rate(22050)
        
        # 转为numpy数组
        samples = np.array(audio.get_array_of_samples()).astype(np.float32)
        samples = samples / np.max(np.abs(samples))
        
        # 提取特征
        features = extract_audio_features(samples)
        
        # 生成嵌入
        embedding = generate_audio_embedding(features)
        
        # 查找相似曲目
        results = find_similar_tracks(embedding, top_k=5)
        
        # 返回结果
        return {
            "status": "success",
            "query_duration": len(samples) / 22050,
            "results": results,
            "processing_time": time.time() - start_time
        }
    
    except Exception as e:
        return {
            "status": "error",
            "message": str(e)
        }
```

### 3. 智能编曲核心API
```java
@RestController
@RequestMapping("/api/v1/composer")
public class MusicComposerController {

    @Autowired
    private AIAudioGenerationService aiAudioGenerationService;
    
    @Autowired
    private MusicStyleClassifier styleClassifier;
    
    @Autowired
    private AudioQualityEnhancer audioEnhancer;
    
    /**
     * 文本到音乐生成
     */
    @PostMapping("/text-to-music")
    public ResponseEntity<CompositionResult> generateMusicFromText(
            @RequestBody @Valid TextToMusicRequest request,
            @RequestHeader("Authorization") String authHeader) {
        
        // 验证权限
        User user = authService.validateToken(authHeader);
        
        // 安全检查
        if (!contentFilterService.isSafeContent(request.getPrompt())) {
            throw new SecurityException("请求包含不安全内容");
        }
        
        try {
            // 1. 解析文本描述，提取音乐特征
            MusicDescription description = musicDescriptionParser.parse(request.getPrompt());
            
            // 2. 确定曲风和情绪
            if (request.getStyle() == null || request.getStyle().isEmpty()) {
                // 如果没有指定曲风，自动分析
                description.setStyle(styleClassifier.classifyFromDescription(request.getPrompt()));
            } else {
                description.setStyle(request.getStyle());
            }
            
            // 3. 生成基础旋律
            MelodyGenerationResult melodyResult = aiAudioGenerationService.generateMelody(
                description, 
                request.getDuration(),
                user.getPreferences()
            );
            
            // 4. 生成和声与伴奏
            ArrangementResult arrangement = aiAudioGenerationService.generateArrangement(
                melodyResult.getMelody(),
                description,
                request.getComplexity()
            );
            
            // 5. 音频合成与增强
            AudioSynthesisResult synthesis = audioSynthesizer.synthesize(
                melodyResult,
                arrangement,
                description.getBpm(),
                description.getKey()
            );
            
            EnhancedAudioResult enhanced = audioEnhancer.enhance(synthesis.getAudioData());
            
            // 6. 保存结果
            Composition composition = new Composition();
            composition.setUserId(user.getId());
            composition.setTitle(description.getTitle() != null ? 
                description.getTitle() : "AI创作 - " + new Date());
            composition.setDescription(request.getPrompt());
            composition.setStyle(description.getStyle());
            composition.setMood(description.getMood());
            composition.setDuration(request.getDuration());
            composition.setAudioData(enhanced.getEnhancedAudio());
            composition.setWaveformData(enhanced.getWaveformData());
            composition.setMetadata(description.toMetadata());
            composition.setCreatedAt(new Date());
            
            compositionRepository.save(composition);
            
            // 7. 准备响应
            CompositionResult result = new CompositionResult();
            result.setId(composition.getId());
            result.setTitle(composition.getTitle());
            result.setAudioUrl("/api/v1/audio/stream/" + composition.getId());
            result.setWaveformData(composition.getWaveformData());
            result.setStyle(composition.getStyle());
            result.setMood(composition.getMood());
            result.setDuration(composition.getDuration());
            result.setCreatedAt(composition.getCreatedAt());
            
            return ResponseEntity.ok(result);
            
        } catch (AudioGenerationException e) {
            throw new ServiceException("音乐生成失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取支持的曲风
     */
    @GetMapping("/supported-styles")
    public ResponseEntity<List<MusicStyle>> getSupportedStyles() {
        List<MusicStyle> styles = styleClassifier.getSupportedStyles();
        return ResponseEntity.ok(styles);
    }
}
```

## 核心数据模型
```
┌───────────────────────┐       ┌───────────────────────────┐
│     User              │1──*───│    UserPreferences        │
├───────────────────────┤       ├───────────────────────────┤
│ user_id (PK)          │       │ preference_id (PK)        │
│ username              │       │ user_id (FK)              │
│ email                 │       │ theme                     │
│ password_hash         │       │ audio_enhancement_level   │
│ created_at            │       │ default_playback_quality  │
│ last_login            │       │ notification_settings     │
│ subscription_tier     │       │ crossfade_duration        │
│ storage_quota         │       │ equalizer_preset          │
└───────────────────────┘       └───────────────────────────┘
        │                               │
        │                               │
┌───────┴─────────────────┐     ┌───────┴─────────────────┐
│     MusicCollection     │     │     ListeningHistory    │
├─────────────────────────┤     ├─────────────────────────┤
│ collection_id (PK)      │     │ history_id (PK)         │
│ user_id (FK)            │     │ user_id (FK)            │
│ name                    │     │ track_id (FK)           │
│ description             │     │ played_at               │
│ created_at              │     │ duration_played         │
│ is_public               │     │ device_info             │
│ cover_image_url         │     │ location                │
└─────────────────────────┘     │ context                 │
        │                       └─────────────────────────┘
        │                               │
┌───────┴─────────────────┐     ┌───────┴─────────────────┐
│   CollectionItem        │     │     Recommendation     │
├─────────────────────────┤     ├─────────────────────────┤
│ item_id (PK)            │     │ recommendation_id (PK)  │
│ collection_id (FK)      │     │ user_id (FK)            │
│ track_id (FK)           │     │ recommended_track_id    │
│ added_at                │     │ reason                  │
│ position                │     │ score                   │
│ notes                   │     │ generated_at            │
└─────────────────────────┘     └─────────────────────────┘
        │
        │
┌───────┴───────────────────────────────────────────────────┐
│                     MusicTrack                            │
├───────────────────────────────────────────────────────────┤
│ track_id (PK)                                             │
│ title                                                     │
│ artist                                                    │
│ album                                                     │
│ duration                                                  │
│ file_path                                                 │
│ file_size                                                 │
│ audio_format                                              │
│ sample_rate                                               │
│ bit_depth                                                 │
│ bpm                                                       │
│ key_signature                                             │
│ genre                                                     │
│ mood_tags[]                                               │
│ source_url                                                │
│ source_type (VIDEO/AUDIO/WEB)                             │
│ copyright_status                                          │
│ fingerprint (audio hash)                                  │
│ wave_data (for visualization)                             │
│ lyrics                                                    │
│ created_at                                                │
│ updated_at                                                │
└───────────────────────────────────────────────────────────┘
        │
        │
┌───────┴─────────────────┐     ┌───────────────────────────┐
│     AudioAnalysis       │     │     AIComposition         │
├─────────────────────────┤     ├───────────────────────────┤
│ analysis_id (PK)        │     │ composition_id (PK)       │
│ track_id (FK)           │     │ user_id (FK)              │
│ spectral_features       │     │ prompt                    │
│ rhythmic_features       │     │ style                     │
│ harmonic_features       │     │ mood                      │
│ timbre_features         │     │ duration                  │
│ emotion_analysis        │     │ audio_data                │
│ created_at              │     │ wave_data                 │
└─────────────────────────┘     │ created_at                │
                                │ metadata                  │
                                └───────────────────────────┘
```

## 开发与交付要求

### 1. 分阶段交付
- **第一阶段（基础音乐库）**：
  - 核心音乐管理（导入/播放/组织）
  - 3个主流平台爬虫（YouTube/B站/SoundCloud）
  - 基础搜索与分类
  - 量子视觉UI框架
- **第二阶段（AI增强）**：
  - 听音辩曲功能
  - 基础智能编曲
  - 高级音频处理
  - 跨设备同步
- **第三阶段（完整体验）**：
  - 全面爬虫支持（10+源站）
  - 高级AI编曲
  - 3D音频可视化
  - 社区功能与排行榜

### 2. 跨平台兼容性
- **PC Web应用**：
  - Chrome/Firefox/Safari/Edge最新两个版本
  - 支持音频工作API
  - 3D WebGL可视化
- **移动应用 (Uniapp)**：
  - iOS 14+ (iPhone 8及以上)
  - Android 10+ (ARM64架构)
  - 后台播放与锁屏控制
  - 低电量优化

### 3. 性能指标
- **爬虫性能**：
  - 1080p视频转音频<30秒/分钟
  - 元数据提取<2秒/资源
  - 增量更新<5分钟/100资源
- **音频处理**：
  - 听音辩曲响应<3秒
  - 音频增强处理<5秒/3分钟
  - 智能编曲生成<60秒/1分钟
- **用户体验**：
  - 界面响应<100ms
  - 音频启动延迟<200ms
  - 3D波形渲染>30FPS

### 4. 版权与安全
- **严格遵守**：
  - 仅处理开源/免费/授权音乐
  - 清晰标注来源与版权信息
  - 提供版权争议处理机制
  - 用户本地存储，不提供公共分享
- **隐私保护**：
  - 音频数据本地加密存储
  - 传输数据TLS 1.3+
  - GDPR/CCPA合规
  - 音频指纹不可逆转换

## 交付物清单
### 1. 源代码
- **后端**：
  - Spring Boot音乐服务
  - 爬虫引擎
  - AI集成模块
  - 音频处理服务
- **前端**：
  - Vue3 Web应用
  - Uniapp移动端应用
  - 量子UI组件库
  - 3D音频可视化库

### 2. 预置内容
- **音乐资源**：
  - 100+开源音乐示例
  - 20+曲风样本库
  - 50+音频处理预设
- **视觉资源**：
  - 5套高科技主题
  - 10+3D粒子效果
  - 音频可视化模板
  - 交互动效库

### 3. 文档
- **开发文档**：
  - 系统架构
  - API文档
  - 部署指南
  - 贡献指南
- **用户文档**：
  - 快速入门
  - 高级功能指南
  - 隐私与版权说明
  - 故障排除

## 演示场景
**必须包含完整演示**：展示一个用户从发现音乐到创作音乐的完整旅程：
1. **音乐发现**：演示爬虫从YouTube提取一段视频音乐，自动识别歌曲，保存到个人库
2. **音乐组织**：展示智能分类，用户创建"专注工作"播放列表，AI推荐相似音乐
3. **听音辩曲**：播放一段环境音乐，系统准确识别并显示歌曲信息
4. **智能编曲**：用户输入"宁静的湖面，轻柔的钢琴与弦乐，黄昏时分"，系统生成并播放定制音乐
5. **跨设备体验**：在手机上开始播放，无缝切换到PC端继续，保持播放进度和设置

**演示必须突出3个核心高科技特性**：
1. 3D音频波形可视化（粒子效果随音乐跳动）
2. AI听音辩曲与智能编曲（自然语言→音乐）
3. 量子视觉界面（光影流动+全息控制）

## 关键技术约束
1. **版权合规**：
   - 严格筛选开源/免费音乐资源
   - 不提供版权内容的直接下载/分享
   - 所有爬取内容仅用于个人使用
   - 提供版权内容过滤机制

2. **性能优化**：
   - 音频处理使用Web Worker
   - 3D可视化使用WebGL性能优化
   - 移动端资源使用限制
   - 后台播放电量优化

3. **离线能力**：
   - 核心音乐库本地缓存
   - 离线听音辩曲（基础版）
   - 离线播放列表
   - 本地AI编曲（简化版）

## 成功标准
交付一个重新定义个人音乐体验的智能平台，实现：
- **技术融合**：无缝整合爬虫/AI/3D可视化技术
- **体验纯粹**：专注于音乐发现与创作，无干扰
- **跨端一致**：PC与移动提供同等高质量体验
- **尊重版权**：建立可长期运营的合规模式

**警示**：本平台必须严格遵守各平台服务条款与版权法。爬虫功能仅用于个人研究与合理使用，不得用于商业分发。所有AI生成内容必须明确标识为AI创作。系统必须在普通配置设备上流畅运行，特别优化移动设备电池消耗。每个功能必须经过严格的用户体验测试，确保音乐爱好者和普通用户都能轻松使用。最终交付物不仅是代码，更是一个能够真正提升用户音乐生活品质的个人音乐宇宙。