package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/crawler")
@CrossOrigin(origins = "*")
public class CrawlerController {
    
    @Autowired
    private CrawlerService crawlerService;
    
    /**
     * 从单个音乐源爬取音乐
     * @param sourceUrl 音乐源URL
     * @param sourceType 音乐源类型
     * @return 爬取到的音乐列表
     */
    @PostMapping("/crawl/single")
    public ResponseEntity<Map<String, Object>> crawlSingleSource(
            @RequestParam String sourceUrl,
            @RequestParam(required = false, defaultValue = "general") String sourceType) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Music> musicList = crawlerService.crawlMusicFromSource(sourceUrl, sourceType);
            
            response.put("code", 200);
            response.put("message", "爬取成功");
            response.put("data", musicList);
            response.put("count", musicList.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "爬取失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 从多个音乐源爬取音乐
     * @param sourceMap 音乐源映射（URL -> 类型）
     * @return 每个音乐源的爬取结果
     */
    @PostMapping("/crawl/multiple")
    public ResponseEntity<Map<String, Object>> crawlMultipleSources(
            @RequestBody Map<String, String> sourceMap) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, List<Music>> results = crawlerService.crawlMusicFromMultipleSources(sourceMap);
            
            // 计算总音乐数量
            int totalCount = results.values().stream()
                    .mapToInt(List::size)
                    .sum();
            
            response.put("code", 200);
            response.put("message", "多源爬取成功");
            response.put("data", results);
            response.put("totalCount", totalCount);
            response.put("sourceCount", results.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "多源爬取失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取支持的音乐源类型
     * @return 支持的音乐源类型列表
     */
    @GetMapping("/source-types")
    public ResponseEntity<Map<String, Object>> getSupportedSourceTypes() {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<String> sourceTypes = crawlerService.getSupportedSourceTypes();
            
            response.put("code", 200);
            response.put("message", "获取成功");
            response.put("data", sourceTypes);
            response.put("count", sourceTypes.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "获取失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 立即执行爬取任务
     * @return 响应结果
     */
    @PostMapping("/execute-now")
    public ResponseEntity<Map<String, Object>> executeCrawlingJobNow() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 这里可以调用实际的爬取方法
            response.put("code", 200);
            response.put("message", "立即执行爬取任务成功");
            response.put("data", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "立即执行爬取任务失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 爬取所有支持的音乐源
     * @return 响应结果
     */
    @PostMapping("/crawl-all")
    public ResponseEntity<Map<String, Object>> crawlAllSupportedSources() {
        Map<String, Object> response = new HashMap<>();
        try {
            crawlerService.crawlAllSupportedSources();
            response.put("code", 200);
            response.put("message", "爬取所有音乐源成功");
            response.put("data", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "爬取所有音乐源失败: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 调度爬取任务
     * @param cronExpression Cron表达式
     * @return 调度结果
     */
    @PostMapping("/schedule")
    public ResponseEntity<Map<String, Object>> scheduleCrawlingJob(
            @RequestParam String cronExpression) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            crawlerService.scheduleCrawlingJob(cronExpression);
            
            response.put("code", 200);
            response.put("message", "爬取任务已调度");
            response.put("data", cronExpression);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "调度失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 执行立即爬取（测试用）
     * @param sourceUrl 音乐源URL
     * @param sourceType 音乐源类型
     * @return 爬取结果
     */
    @PostMapping("/crawl/immediate")
    public ResponseEntity<Map<String, Object>> crawlImmediate(
            @RequestParam String sourceUrl,
            @RequestParam(required = false, defaultValue = "general") String sourceType) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Music> musicList = crawlerService.crawlMusicFromSource(sourceUrl, sourceType);
            
            response.put("code", 200);
            response.put("message", "立即爬取成功");
            response.put("data", musicList);
            response.put("count", musicList.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "立即爬取失败: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}