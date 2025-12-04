package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    
    /**
     * 从本地视频文件提取音乐
     * @param videoFile 视频文件
     * @return 提取的音乐信息
     */
    @PostMapping("/extract/video-file")
    public ResponseEntity<Map<String, Object>> extractMusicFromVideoFile(
            @RequestParam("videoFile") MultipartFile videoFile) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存上传的视频文件到临时目录
            File tempVideoFile = File.createTempFile("uploaded_video_", ".mp4");
            tempVideoFile.deleteOnExit();
            videoFile.transferTo(tempVideoFile);
            
            // 提取音乐
            Music music = crawlerService.extractMusicFromVideoFile(tempVideoFile);
            
            if (music != null) {
                response.put("code", 200);
                response.put("message", "音乐提取成功");
                response.put("data", music);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "音乐提取失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音乐提取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 从视频链接提取音乐
     * @param videoUrl 视频链接
     * @return 提取的音乐信息
     */
    @PostMapping("/extract/video-url")
    public ResponseEntity<Map<String, Object>> extractMusicFromVideoUrl(
            @RequestParam String videoUrl) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Music music = crawlerService.extractMusicFromVideoUrl(videoUrl);
            
            if (music != null) {
                response.put("code", 200);
                response.put("message", "音乐提取成功");
                response.put("data", music);
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "音乐提取失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音乐提取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 音乐搜索功能
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMusic(
            @RequestParam String keyword) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Music> searchResults = crawlerService.searchMusic(keyword);
            
            response.put("code", 200);
            response.put("message", "搜索成功");
            response.put("data", searchResults);
            response.put("count", searchResults.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 音频剪切功能
     * @param audioFile 音频文件
     * @param startTime 开始时间（秒）
     * @param endTime 结束时间（秒）
     * @return 剪切后的音频文件路径
     */
    @PostMapping("/edit/cut")
    public ResponseEntity<Map<String, Object>> cutAudio(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam int startTime,
            @RequestParam int endTime) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存上传的音频文件到临时目录
            File tempAudioFile = File.createTempFile("uploaded_audio_", ".mp3");
            tempAudioFile.deleteOnExit();
            audioFile.transferTo(tempAudioFile);
            
            // 剪切音频
            File cutFile = crawlerService.cutAudio(tempAudioFile, startTime, endTime);
            
            if (cutFile != null && cutFile.exists()) {
                response.put("code", 200);
                response.put("message", "音频剪切成功");
                response.put("data", cutFile.getAbsolutePath());
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "音频剪切失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音频剪切失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 添加音效功能
     * @param audioFile 音频文件
     * @param effectType 音效类型
     * @return 添加音效后的音频文件路径
     */
    @PostMapping("/edit/effect")
    public ResponseEntity<Map<String, Object>> addAudioEffect(
            @RequestParam("audioFile") MultipartFile audioFile,
            @RequestParam String effectType) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 保存上传的音频文件到临时目录
            File tempAudioFile = File.createTempFile("uploaded_audio_", ".mp3");
            tempAudioFile.deleteOnExit();
            audioFile.transferTo(tempAudioFile);
            
            // 添加音效
            File effectFile = crawlerService.addAudioEffect(tempAudioFile, effectType);
            
            if (effectFile != null && effectFile.exists()) {
                response.put("code", 200);
                response.put("message", "音效添加成功");
                response.put("data", effectFile.getAbsolutePath());
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "音效添加失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音效添加失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}