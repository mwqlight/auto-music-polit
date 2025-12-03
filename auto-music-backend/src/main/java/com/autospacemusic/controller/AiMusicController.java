package com.autospacemusic.controller;

import com.autospacemusic.service.AiMusicService;
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
    public ResponseEntity<String> recognizeMusic(@RequestParam("audio") MultipartFile audioFile) {
        try {
            byte[] audioData = audioFile.getBytes();
            String result = aiMusicService.recognizeMusic(audioData);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("音频文件处理失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateMusic(@RequestBody GenerateMusicRequest request) {
        try {
            byte[] musicData = aiMusicService.generateMusic(request.getStyle(), request.getMood(), request.getDuration());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "generated_music.wav");
            
            return new ResponseEntity<>(musicData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("音乐生成失败: " + e.getMessage()).getBytes());
        }
    }
    
    @PostMapping("/remix")
    public ResponseEntity<byte[]> remixMusic(@RequestParam("audio") MultipartFile audioFile,
                                           @RequestParam("style") String style) {
        try {
            byte[] originalMusic = audioFile.getBytes();
            byte[] remixedMusic = aiMusicService.remixMusic(originalMusic, style);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "remixed_music.wav");
            
            return new ResponseEntity<>(remixedMusic, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("音乐混音失败: " + e.getMessage()).getBytes());
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