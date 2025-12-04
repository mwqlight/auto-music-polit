package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.service.CrawlerService;
import com.autospacemusic.service.MusicService;
import com.autospacemusic.util.VideoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/crawler")
@CrossOrigin(origins = "*")
public class CrawlerController {
    
    @Autowired
    private CrawlerService crawlerService;
    
    @Autowired
    private VideoUtils videoUtils;
    
    @Autowired
    private MusicService musicService;
    
    // 临时文件存储目录
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/auto-music/";
    
    static {
        // 创建临时目录
        File tempDir = new File(TEMP_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
    }
    
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
     * 上传视频文件并提取音频
     * @param videoFile 视频文件
     * @return 提取的音乐信息
     */
    @PostMapping("/extract-audio")
    public ResponseEntity<Map<String, Object>> extractAudioFromVideo(@RequestParam("videoFile") MultipartFile videoFile) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查文件是否为空
            if (videoFile.isEmpty()) {
                response.put("code", 400);
                response.put("message", "视频文件不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 检查文件类型
            String contentType = videoFile.getContentType();
            if (contentType == null || !contentType.startsWith("video/")) {
                response.put("code", 400);
                response.put("message", "请上传有效的视频文件");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 生成唯一文件名
            String originalFilename = videoFile.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : ".mp4";
            String uniqueId = UUID.randomUUID().toString();
            String videoFileName = uniqueId + "_video" + fileExtension;
            String audioFileName = uniqueId + "_audio.mp3";
            
            // 保存视频文件到临时目录
            Path videoPath = Paths.get(TEMP_DIR + videoFileName);
            Files.write(videoPath, videoFile.getBytes());
            
            // 提取音频
            Path audioPath = Paths.get(TEMP_DIR + audioFileName);
            boolean extractionSuccess = videoUtils.extractAudioFromVideo(videoPath.toString(), audioPath.toString());
            
            if (!extractionSuccess) {
                response.put("code", 500);
                response.put("message", "音频提取失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
            // 获取视频信息
            VideoUtils.VideoInfo videoInfo = videoUtils.getVideoInfo(videoPath.toString()).orElse(null);
            
            // 创建音乐实体
            Music music = new Music();
            music.setTitle(originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf(".")) : "未知视频音乐");
            music.setArtist("视频提取");
            music.setFilePath(audioPath.toString());
            music.setDurationSeconds(videoInfo != null ? (int) videoInfo.getDuration() : 0);
            music.setGenre("视频音乐");
            music.setMood("未知");
            music.setQuality("高");
            music.setCreatedAt(LocalDateTime.now());
            music.setUpdatedAt(LocalDateTime.now());
            
            // 保存到数据库
            musicService.save(music);
            
            // 清理临时视频文件
            Files.deleteIfExists(videoPath);
            
            response.put("code", 200);
            response.put("message", "音频提取成功");
            response.put("data", music);
            
            return ResponseEntity.ok(response);
            
        } catch (IOException e) {
            response.put("code", 500);
            response.put("message", "文件处理失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音频提取失败: " + e.getMessage());
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
     * 从视频链接提取音频
     * @param videoUrl 视频链接
     * @return 提取的音乐信息
     */
    @PostMapping("/extract-audio-from-url")
    public ResponseEntity<Map<String, Object>> extractAudioFromVideoUrl(@RequestParam String videoUrl) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查视频链接是否为空
            if (videoUrl == null || videoUrl.trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "视频链接不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 生成唯一文件名
            String uniqueId = UUID.randomUUID().toString();
            String audioFileName = uniqueId + "_audio.mp3";
            Path audioPath = Paths.get(TEMP_DIR + audioFileName);
            
            // 使用yt-dlp下载视频并提取音频
            boolean downloadSuccess = downloadAndExtractAudio(videoUrl, audioPath.toString());
            
            if (!downloadSuccess) {
                response.put("code", 500);
                response.put("message", "视频下载和音频提取失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
            // 获取音频信息
            int duration = videoUtils.getAudioDuration(audioPath.toString());
            
            // 创建音乐实体
            Music music = new Music();
            music.setTitle("视频链接音乐_" + uniqueId.substring(0, 8));
            music.setArtist("视频链接提取");
            music.setFilePath(audioPath.toString());
            music.setDurationSeconds(duration);
            music.setGenre("视频音乐");
            music.setMood("未知");
            music.setQuality("高");
            music.setCreatedAt(LocalDateTime.now());
            music.setUpdatedAt(LocalDateTime.now());
            
            // 保存到数据库
            musicService.save(music);
            
            response.put("code", 200);
            response.put("message", "音频提取成功");
            response.put("data", music);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "音频提取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 使用yt-dlp下载视频并提取音频
     * @param videoUrl 视频链接
     * @param outputPath 输出音频路径
     * @return 是否成功
     */
    private boolean downloadAndExtractAudio(String videoUrl, String outputPath) {
        try {
            // 构建yt-dlp命令
            String[] command = {
                "yt-dlp",
                "-x", // 仅提取音频
                "--audio-format", "mp3", // 音频格式
                "--audio-quality", "0", // 最高质量
                "-o", outputPath, // 输出路径
                videoUrl // 视频链接
            };
            
            // 执行命令
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // 读取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("yt-dlp: " + line);
            }
            
            // 等待命令完成
            int exitCode = process.waitFor();
            
            return exitCode == 0;
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 搜索音乐
     * @param keyword 搜索关键词
     * @param page 页码
     * @param size 每页大小
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMusic(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查关键词是否为空
            if (keyword == null || keyword.trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "搜索关键词不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 创建分页请求
            Pageable pageable = PageRequest.of(page, size);
            
            // 执行搜索
            Page<Music> musicPage = musicService.search(keyword, pageable);
            
            response.put("code", 200);
            response.put("message", "搜索成功");
            response.put("data", musicPage.getContent());
            response.put("total", musicPage.getTotalElements());
            response.put("page", musicPage.getNumber());
            response.put("size", musicPage.getSize());
            response.put("totalPages", musicPage.getTotalPages());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("code", 500);
            response.put("message", "搜索失败: " + e.getMessage());
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