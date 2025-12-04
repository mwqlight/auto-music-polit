package com.autospacemusic.controller;

import com.autospacemusic.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    /**
     * 从指定源URL爬取音乐
     * @param sourceUrl 源URL
     * @return 爬取到的音乐URL列表
     */
    @PostMapping("/crawl")
    public ResponseEntity<List<String>> crawlMusicFromSource(@RequestParam String sourceUrl) {
        List<String> musicUrls = crawlerService.crawlMusicFromSource(sourceUrl);
        return ResponseEntity.ok(musicUrls);
    }

    /**
     * 调度爬取任务
     * @param cronExpression cron表达式
     * @return 调度结果
     */
    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleCrawlingJob(@RequestParam String cronExpression) {
        crawlerService.scheduleCrawlingJob(cronExpression);
        return ResponseEntity.ok("爬取任务已调度");
    }

    /**
     * 执行每日爬取任务
     * @return 爬取结果
     */
    @PostMapping("/daily-crawl")
    public ResponseEntity<String> executeDailyCrawlingJob() {
        crawlerService.dailyCrawlingJob();
        return ResponseEntity.ok("每日爬取任务已执行");
    }
}
