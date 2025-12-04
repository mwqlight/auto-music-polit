package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Music;
import com.autospacemusic.service.CrawlerService;
import com.autospacemusic.service.MusicService;
import com.autospacemusic.util.AudioUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    @Override
    public Music extractMusicFromVideoFile(File videoFile) {
        // 从本地视频文件提取音乐
        try {
            // 使用FFmpeg或其他工具提取音频
            String audioFilePath = AudioUtils.extractAudioFromVideo(videoFile.getAbsolutePath());
            
            // 创建音乐对象
            Music music = new Music();
            music.setTitle(extractTitleFromFilename(videoFile.getName()));
            music.setArtist("未知艺术家");
            music.setFilePath(audioFilePath);
            music.setDurationSeconds(AudioUtils.getAudioDuration(audioFilePath));
            music.setGenre("未知");
            music.setMood("未知");
            music.setQuality("高");
            music.setCreatedAt(LocalDateTime.now());
            music.setUpdatedAt(LocalDateTime.now());
            
            // 分析音乐特征
            analyzeAndClassifyMusic(music);
            
            return music;
        } catch (Exception e) {
            System.err.println("从视频文件提取音乐失败: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Music extractMusicFromVideoUrl(String videoUrl) {
        // 从视频链接提取音乐
        try {
            // 下载视频到本地临时文件
            File tempVideoFile = downloadVideoFromUrl(videoUrl);
            
            // 提取音频
            Music music = extractMusicFromVideoFile(tempVideoFile);
            
            // 设置音乐标题为视频标题
            music.setTitle(extractTitleFromVideoUrl(videoUrl));
            
            // 删除临时文件
            tempVideoFile.delete();
            
            return music;
        } catch (Exception e) {
            System.err.println("从视频链接提取音乐失败: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Music> searchMusic(String keyword) {
        // 全网音乐搜索功能
        List<Music> searchResults = new ArrayList<>();
        
        try {
            // 搜索多个音乐源
            Map<String, String> searchSources = new HashMap<>();
            searchSources.put("https://www.youtube.com/results?search_query=" + keyword, "youtube");
            searchSources.put("https://soundcloud.com/search?q=" + keyword, "soundcloud");
            searchSources.put("https://bandcamp.com/search?q=" + keyword, "bandcamp");
            
            Map<String, List<Music>> results = crawlMusicFromMultipleSources(searchSources);
            
            // 合并结果
            for (List<Music> musicList : results.values()) {
                searchResults.addAll(musicList);
            }
            
            // 去重（根据标题和艺术家）
            List<Music> uniqueResults = new ArrayList<>();
            Map<String, Boolean> seen = new HashMap<>();
            
            for (Music music : searchResults) {
                String key = music.getTitle() + "/" + music.getArtist();
                if (!seen.containsKey(key)) {
                    seen.put(key, true);
                    uniqueResults.add(music);
                }
            }
            
            return uniqueResults;
        } catch (Exception e) {
            System.err.println("音乐搜索失败: " + e.getMessage());
            return searchResults;
        }
    }
    
    @Override
    public File cutAudio(File audioFile, int startTime, int endTime) {
        // 音频剪切功能
        try {
            return AudioUtils.cutAudio(audioFile, startTime, endTime);
        } catch (Exception e) {
            System.err.println("音频剪切失败: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public File mergeAudios(List<File> audioFiles) {
        // 音频合并功能
        try {
            return AudioUtils.mergeAudios(audioFiles);
        } catch (Exception e) {
            System.err.println("音频合并失败: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public File addAudioEffect(File audioFile, String effectType) {
        // 添加音效功能
        try {
            return AudioUtils.addAudioEffect(audioFile, effectType);
        } catch (Exception e) {
            System.err.println("添加音效失败: " + e.getMessage());
            return null;
        }
    }
    
    // 从文件名提取标题
    private String extractTitleFromFilename(String filename) {
        // 移除文件扩展名
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex != -1) {
            filename = filename.substring(0, lastDotIndex);
        }
        
        // 替换下划线和连字符为空格
        return filename.replaceAll("[_-]", " ").trim();
    }
    
    // 从视频链接提取标题
    private String extractTitleFromVideoUrl(String videoUrl) throws IOException {
        Document document = Jsoup.connect(videoUrl)
                .userAgent("Mozilla/5.0")
                .timeout(10000)
                .get();
        
        Element titleElement = document.selectFirst("title");
        if (titleElement != null) {
            String title = titleElement.text();
            // 移除常见的视频网站后缀
            title = title.replaceAll("\\| YouTube", "")
                        .replaceAll("\\| SoundCloud", "")
                        .replaceAll("\\| Bandcamp", "")
                        .trim();
            return title;
        }
        
        return "未知标题";
    }
    
    // 从视频链接下载视频到本地临时文件
    private File downloadVideoFromUrl(String videoUrl) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("video_", ".mp4");
        tempFile.deleteOnExit();
        
        // 使用Jsoup或其他工具下载视频
        // 这里只是示例，实际需要使用更专业的视频下载库
        System.out.println("正在下载视频: " + videoUrl);
        
        return tempFile;
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