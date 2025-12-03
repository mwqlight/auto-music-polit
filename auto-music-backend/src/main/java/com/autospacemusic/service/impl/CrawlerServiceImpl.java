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
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CrawlerServiceImpl implements CrawlerService {
    
    @Autowired
    private MusicService musicService;
    
    @Override
    public List<String> crawlMusicFromSource(String sourceUrl) {
        List<String> musicUrls = new ArrayList<>();
        
        try {
            // 使用Jsoup爬取网页内容
            Document doc = Jsoup.connect(sourceUrl)
                    .timeout(10000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();
            
            // 根据实际网站结构调整选择器
            Elements links = doc.select("a[href$=.mp3]"); // 选择所有指向MP3文件的链接
            
            for (Element link : links) {
                String url = link.absUrl("href");
                if (!url.isEmpty()) {
                    musicUrls.add(url);
                }
            }
        } catch (IOException e) {
            System.err.println("爬取音乐资源时发生错误: " + e.getMessage());
        }
        
        return musicUrls;
    }
    
    @Override
    public void scheduleCrawlingJob(String cronExpression) {
        System.out.println("Scheduled crawling job with cron: " + cronExpression);
    }
    
    // 示例定时任务，每天凌晨2点执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailyCrawlingJob() {
        System.out.println("Executing daily crawling job at: " + LocalDateTime.now());
        
        // 爬取示例音乐网站
        List<String> musicUrls = crawlMusicFromSource("https://example-music-site.com");
        
        // 保存爬取到的音乐信息到数据库
        for (String url : musicUrls) {
            Music music = new Music();
            music.setTitle(extractTitleFromUrl(url));
            music.setArtist("Unknown Artist");
            music.setFilePath(url);
            music.setDurationSeconds(0); // 实际应用中需要解析音频文件获取时长
            music.setCreatedAt(LocalDateTime.now());
            music.setUpdatedAt(LocalDateTime.now());
            
            musicService.save(music);
        }
        
        System.out.println("Daily crawling job completed. Added " + musicUrls.size() + " music tracks.");
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