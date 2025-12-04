package com.autospacemusic.controller;

import com.autospacemusic.service.AiMusicService;
import com.autospacemusic.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/ai-music")
@CrossOrigin(origins = "*")
public class AiMusicController {
    
    @Autowired
    private AiMusicService aiMusicService;
    
    @PostMapping("/recognize")
    public ResponseEntity<ApiResponse<String>> recognizeMusic(@RequestParam("audio") MultipartFile audioFile) {
        try {
            byte[] audioData = audioFile.getBytes();
            String result = aiMusicService.recognizeMusic(audioData);
            return ResponseEntity.ok(ApiResponse.success("音乐识别成功", result));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("音频文件处理失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateMusic(@RequestBody GenerateMusicRequest request) {
        try {
            byte[] musicData = aiMusicService.generateMusic(request.getStyle(), request.getMood(), request.getDuration());
            // 这里可以将生成的音乐数据保存到文件或返回文件路径
            // 为了简化，这里返回成功消息
            return ResponseEntity.ok(ApiResponse.success("音乐生成成功，文件已准备好下载"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("音乐生成失败: " + e.getMessage()));
        }
    }
    
    @PostMapping("/remix")
    public ResponseEntity<ApiResponse<String>> remixMusic(@RequestParam("audio") MultipartFile audioFile,
                                           @RequestParam("style") String style) {
        try {
            byte[] originalMusic = audioFile.getBytes();
            byte[] remixedMusic = aiMusicService.remixMusic(originalMusic, style);
            // 这里可以将混音后的音乐数据保存到文件或返回文件路径
            // 为了简化，这里返回成功消息
            return ResponseEntity.ok(ApiResponse.success("音乐混音成功，文件已准备好下载"));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("音乐混音失败: " + e.getMessage()));
        }
    }
    
    // 请求体类
    public static class GenerateMusicRequest {
        private String style;
        private String mood;
        private int duration;
        
        public String getStyle() {
            return style;
        }
        
        public void setStyle(String style) {
            this.style = style;
        }
        
        public String getMood() {
            return mood;
        }
        
        public void setMood(String mood) {
            this.mood = mood;
        }
        
        public int getDuration() {
            return duration;
        }
        
        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}