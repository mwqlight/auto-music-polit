package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Music;
import com.autospacemusic.service.CrawlerService;
import com.autospacemusic.service.MusicService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CrawlerServiceImpl implements CrawlerService {
    
    @Autowired
    private MusicService musicService;
    
    // 支持的音乐源类型
    private static final List<String> SUPPORTED_SOURCE_TYPES = List.of(
        "general", // 通用音乐网站
        "youtube", // YouTube
        "soundcloud", // SoundCloud
        "bandcamp", // Bandcamp
        "free-music-archive", // Free Music Archive
        "musopen" // Musopen
    );
    
    @Override
    public List<Music> crawlMusicFromSource(String sourceUrl, String sourceType) {
        List<Music> musicList = new ArrayList<>();
        
        try {
            // 根据源类型选择不同的爬取策略
            switch (sourceType.toLowerCase()) {
                case "youtube":
                    musicList = crawlYouTube(sourceUrl);
                    break;
                case "soundcloud":
                    musicList = crawlSoundCloud(sourceUrl);
                    break;
                case "bandcamp":
                    musicList = crawlBandcamp(sourceUrl);
                    break;
                case "free-music-archive":
                    musicList = crawlFreeMusicArchive(sourceUrl);
                    break;
                case "musopen":
                    musicList = crawlMusopen(sourceUrl);
                    break;
                case "general":
                default:
                    musicList = crawlGeneralMusicSite(sourceUrl);
                    break;
            }
            
            // 对爬取到的音乐进行智能分析和分类
            for (Music music : musicList) {
                analyzeAndClassifyMusic(music);
            }
            
        } catch (IOException e) {
            System.err.println("爬取音乐资源时发生错误: " + e.getMessage());
        }
        
        return musicList;
    }
    
    @Override
    public Map<String, List<Music>> crawlMusicFromMultipleSources(Map<String, String> sourceMap) {
        Map<String, List<Music>> results = new HashMap<>();
        
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            String sourceUrl = entry.getKey();
            String sourceType = entry.getValue();
            
            List<Music> musicList = crawlMusicFromSource(sourceUrl, sourceType);
            results.put(sourceUrl, musicList);
        }
        
        return results;
    }
    
    @Override
    public void analyzeAndClassifyMusic(Music music) {
        // 智能内容识别和分类逻辑
        // 这里可以集成音频分析库（如Librosa）来分析音乐的特征
        
        // 示例：根据标题和URL进行简单的分类
        String title = music.getTitle().toLowerCase();
        String url = music.getFilePath().toLowerCase();
        
        // 简单的流派分类
        if (title.contains("rock") || url.contains("rock")) {
            music.setGenre("摇滚");
        } else if (title.contains("pop") || url.contains("pop")) {
            music.setGenre("流行");
        } else if (title.contains("jazz") || url.contains("jazz")) {
            music.setGenre("爵士");
        } else if (title.contains("classical") || url.contains("classical")) {
            music.setGenre("古典");
        } else if (title.contains("electronic") || url.contains("electronic")) {
            music.setGenre("电子");
        } else {
            music.setGenre("其他");
        }
        
        // 简单的情绪分类
        if (title.contains("happy") || title.contains("joy") || title.contains("cheer")) {
            music.setMood("欢快");
        } else if (title.contains("sad") || title.contains("melancholy") || title.contains("blue")) {
            music.setMood("悲伤");
        } else if (title.contains("relax") || title.contains("calm") || title.contains("peace")) {
            music.setMood("放松");
        } else if (title.contains("energetic") || title.contains("power") || title.contains("strong")) {
            music.setMood("激情");
        } else {
            music.setMood("中性");
        }
        
        // 简单的音质评估
        if (url.contains("high-quality") || url.contains("hq") || url.contains("flac")) {
            music.setQuality("高");
        } else if (url.contains("medium-quality") || url.contains("mq")) {
            music.setQuality("中");
        } else {
            music.setQuality("低");
        }
    }
    
    @Override
    public void scheduleCrawlingJob(String cronExpression) {
        System.out.println("Scheduled crawling job with cron: " + cronExpression);
        // 实际应用中可以使用Quartz或Spring Scheduler来实现定时任务
    }
    
    @Override
    public void crawlAllSupportedSources() {
        System.out.println("Crawling all supported music sources...");
        
        // 这里可以添加实际的多源爬取逻辑
        // 示例：爬取几个示例音乐源
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("https://example-music-site.com", "general");
        sourceMap.put("https://soundcloud.com/example", "soundcloud");
        sourceMap.put("https://bandcamp.com/example", "bandcamp");
        
        Map<String, List<Music>> results = crawlMusicFromMultipleSources(sourceMap);
        
        // 保存爬取到的音乐信息到数据库
        for (List<Music> musicList : results.values()) {
            for (Music music : musicList) {
                music.setCreatedAt(LocalDateTime.now());
                music.setUpdatedAt(LocalDateTime.now());
                musicService.save(music);
            }
        }
        
        System.out.println("Crawling of all supported sources completed.");
    }
    
    @Override
    public List<String> getSupportedSourceTypes() {
        return SUPPORTED_SOURCE_TYPES;
    }
    
    // 示例定时任务，每天凌晨2点执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyCrawlingJob() {
        System.out.println("Executing daily crawling job at: " + LocalDateTime.now());
        
        // 爬取多个音乐源
        Map<String, String> sourceMap = new HashMap<>();
        sourceMap.put("https://example-music-site.com", "general");
        sourceMap.put("https://soundcloud.com/example", "soundcloud");
        sourceMap.put("https://bandcamp.com/example", "bandcamp");
        
        Map<String, List<Music>> results = crawlMusicFromMultipleSources(sourceMap);
        
        // 保存爬取到的音乐信息到数据库
        for (List<Music> musicList : results.values()) {
            for (Music music : musicList) {
                music.setCreatedAt(LocalDateTime.now());
                music.setUpdatedAt(LocalDateTime.now());
                musicService.save(music);
            }
        }
        
        System.out.println("Daily crawling job completed.");
    }
    
    /**
     * 从通用音乐网站爬取音乐
     */
    private List<Music> crawlGeneralMusicSite(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 选择所有指向MP3文件的链接
        Elements links = doc.select("a[href$=.mp3]");
        
        for (Element link : links) {
            String url = link.absUrl("href");
            if (!url.isEmpty()) {
                Music music = new Music();
                music.setTitle(extractTitleFromUrl(url));
                music.setArtist("Unknown Artist");
                music.setFilePath(url);
                music.setDurationSeconds(0); // 实际应用中需要解析音频文件获取时长
                musicList.add(music);
            }
        }
        
        return musicList;
    }
    
    /**
     * 从YouTube爬取音乐
     */
    private List<Music> crawlYouTube(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        // YouTube爬取需要特殊处理，这里只是示例
        // 实际应用中可以使用YouTube Data API或专门的爬虫库
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 提取视频标题和信息
        Element titleElement = doc.selectFirst("h1.title");
        if (titleElement != null) {
            Music music = new Music();
            music.setTitle(titleElement.text());
            music.setArtist("YouTube Artist");
            music.setFilePath(sourceUrl);
            music.setDurationSeconds(0);
            musicList.add(music);
        }
        
        return musicList;
    }
    
    /**
     * 从SoundCloud爬取音乐
     */
    private List<Music> crawlSoundCloud(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        // SoundCloud爬取需要特殊处理，这里只是示例
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 提取音乐信息
        Element titleElement = doc.selectFirst("h1");
        Element artistElement = doc.selectFirst("a.user-name");
        
        if (titleElement != null && artistElement != null) {
            Music music = new Music();
            music.setTitle(titleElement.text());
            music.setArtist(artistElement.text());
            music.setFilePath(sourceUrl);
            music.setDurationSeconds(0);
            musicList.add(music);
        }
        
        return musicList;
    }
    
    /**
     * 从Bandcamp爬取音乐
     */
    private List<Music> crawlBandcamp(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        // Bandcamp爬取需要特殊处理，这里只是示例
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 提取音乐信息
        Element titleElement = doc.selectFirst("h2.trackTitle");
        Element artistElement = doc.selectFirst("span.artist");
        
        if (titleElement != null && artistElement != null) {
            Music music = new Music();
            music.setTitle(titleElement.text());
            music.setArtist(artistElement.text());
            music.setFilePath(sourceUrl);
            music.setDurationSeconds(0);
            musicList.add(music);
        }
        
        return musicList;
    }
    
    /**
     * 从Free Music Archive爬取音乐
     */
    private List<Music> crawlFreeMusicArchive(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        // Free Music Archive爬取需要特殊处理，这里只是示例
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 提取音乐信息
        Elements trackElements = doc.select("div.track-item");
        
        for (Element trackElement : trackElements) {
            Element titleElement = trackElement.selectFirst("a.track-title");
            Element artistElement = trackElement.selectFirst("a.artist-name");
            
            if (titleElement != null && artistElement != null) {
                Music music = new Music();
                music.setTitle(titleElement.text());
                music.setArtist(artistElement.text());
                music.setFilePath(titleElement.absUrl("href"));
                music.setDurationSeconds(0);
                musicList.add(music);
            }
        }
        
        return musicList;
    }
    
    /**
     * 从Musopen爬取音乐
     */
    private List<Music> crawlMusopen(String sourceUrl) throws IOException {
        List<Music> musicList = new ArrayList<>();
        
        // Musopen爬取需要特殊处理，这里只是示例
        
        Document doc = Jsoup.connect(sourceUrl)
                .timeout(10000)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .get();
        
        // 提取音乐信息
        Elements trackElements = doc.select("div.track");
        
        for (Element trackElement : trackElements) {
            Element titleElement = trackElement.selectFirst("h3.track-title");
            Element artistElement = trackElement.selectFirst("span.artist");
            
            if (titleElement != null && artistElement != null) {
                Music music = new Music();
                music.setTitle(titleElement.text());
                music.setArtist(artistElement.text());
                music.setFilePath(sourceUrl);
                music.setDurationSeconds(0);
                musicList.add(music);
            }
        }
        
        return musicList;
    }
    
    /**
     * 从URL中提取音乐标题
     */
    private String extractTitleFromUrl(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        }
        return fileName.replace("-", " ").replace("_", " ");
    }
}