package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.dto.request.MusicRequestDto;
import com.autospacemusic.dto.response.MusicResponseDto;
import com.autospacemusic.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/music")
@CrossOrigin(origins = "*")
public class MusicController {
    
    @Autowired
    private MusicService musicService;
    
    // Helper method to convert Music entity to MusicResponseDto
    private MusicResponseDto convertToResponseDto(Music music) {
        MusicResponseDto dto = new MusicResponseDto();
        dto.setId(music.getId());
        dto.setTitle(music.getTitle());
        dto.setArtist(music.getArtist());
        dto.setFilePath(music.getFilePath());
        dto.setDurationSeconds(music.getDurationSeconds());
        dto.setCreatedAt(music.getCreatedAt());
        dto.setUpdatedAt(music.getUpdatedAt());
        dto.setAudioFingerprint(music.getAudioFingerprint());
        dto.setAiGenerated(music.getAiGenerated());
        dto.setGenre(music.getGenre());
        dto.setMood(music.getMood());
        dto.setQuality(music.getQuality());
        return dto;
    }
    
    // Helper method to convert MusicRequestDto to Music entity
    private Music convertToEntity(MusicRequestDto musicDto) {
        Music music = new Music();
        music.setTitle(musicDto.getTitle());
        music.setArtist(musicDto.getArtist());
        music.setFilePath(musicDto.getFilePath());
        music.setDurationSeconds(musicDto.getDurationSeconds());
        music.setAudioFingerprint(musicDto.getAudioFingerprint());
        music.setAiGenerated(musicDto.getAiGenerated());
        music.setGenre(musicDto.getGenre());
        music.setMood(musicDto.getMood());
        music.setQuality(musicDto.getQuality());
        return music;
    }
    
    /**
     * 获取音乐列表（分页、排序、过滤）
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向
     * @param genre 流派过滤
     * @param mood 情绪过滤
     * @param aiGenerated AI生成过滤
     * @return 分页音乐列表
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String mood,
            @RequestParam(required = false) Boolean aiGenerated) {
        
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Music> musicPage;
        
        // 根据过滤条件查询
        if (genre != null && mood != null && aiGenerated != null) {
            musicPage = musicService.findByGenreAndMoodAndAiGenerated(genre, mood, aiGenerated, pageable);
        } else if (genre != null && mood != null) {
            musicPage = musicService.findByGenreAndMood(genre, mood, pageable);
        } else if (genre != null && aiGenerated != null) {
            musicPage = musicService.findByGenreAndAiGenerated(genre, aiGenerated, pageable);
        } else if (mood != null && aiGenerated != null) {
            musicPage = musicService.findByMoodAndAiGenerated(mood, aiGenerated, pageable);
        } else if (genre != null) {
            musicPage = musicService.findByGenre(genre, pageable);
        } else if (mood != null) {
            musicPage = musicService.findByMood(mood, pageable);
        } else if (aiGenerated != null) {
            musicPage = musicService.findByAiGenerated(aiGenerated, pageable);
        } else {
            musicPage = musicService.findAll(pageable);
        }
        
        List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("content", musicDtos);
        response.put("page", musicPage.getNumber());
        response.put("size", musicPage.getSize());
        response.put("totalElements", musicPage.getTotalElements());
        response.put("totalPages", musicPage.getTotalPages());
        response.put("last", musicPage.isLast());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 根据ID获取音乐详情
     * @param id 音乐ID
     * @return 音乐详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMusicById(@PathVariable Long id) {
        
        Map<String, Object> response = new HashMap<>();
        
        return musicService.findById(id)
                .map(music -> {
                    response.put("data", convertToResponseDto(music));
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("message", "音乐不存在");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
    
    /**
     * 创建音乐
     * @param musicDto 音乐请求DTO
     * @return 创建的音乐
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMusic(@RequestBody MusicRequestDto musicDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Music music = convertToEntity(musicDto);
            Music savedMusic = musicService.save(music);
            
            response.put("data", convertToResponseDto(savedMusic));
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 更新音乐
     * @param id 音乐ID
     * @param musicDto 音乐请求DTO
     * @return 更新后的音乐
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMusic(@PathVariable Long id, @RequestBody MusicRequestDto musicDto) {
        
        Map<String, Object> response = new HashMap<>();
        
        return musicService.findById(id)
                .map(existingMusic -> {
                    try {
                        Music music = convertToEntity(musicDto);
                        music.setId(id);
                        music.setCreatedAt(existingMusic.getCreatedAt()); // 保留创建时间
                        
                        Music updatedMusic = musicService.save(music);
                        
                        response.put("data", convertToResponseDto(updatedMusic));
                        
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        response.put("message", "更新失败: " + e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                })
                .orElseGet(() -> {
                    response.put("message", "音乐不存在");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
    
    /**
     * 获取所有音乐质量
     * @return 音乐质量列表
     */
    @GetMapping("/qualities")
    public ResponseEntity<Map<String, Object>> getAllQualities() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<String> qualities = musicService.findAllQualities();
            response.put("data", qualities);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取音乐质量列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 删除音乐
     * @param id 音乐ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMusic(@PathVariable Long id) {
        
        Map<String, Object> response = new HashMap<>();
        
        return musicService.findById(id)
                .map(music -> {
                    try {
                        musicService.deleteById(id);
                        
                        return ResponseEntity.ok(response);
                    } catch (Exception e) {
                        response.put("message", "删除失败: " + e.getMessage());
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                    }
                })
                .orElseGet(() -> {
                    response.put("message", "音乐不存在");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }
    
    /**
     * 搜索音乐
     * @param keyword 搜索关键词
     * @param page 页码
     * @param size 每页数量
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchMusic(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Music> musicPage = musicService.search(keyword, pageable);
            
            List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            response.put("content", musicDtos);
            response.put("page", musicPage.getNumber());
            response.put("size", musicPage.getSize());
            response.put("totalElements", musicPage.getTotalElements());
            response.put("totalPages", musicPage.getTotalPages());
            response.put("last", musicPage.isLast());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "搜索失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 根据音频指纹获取音乐
     * @param fingerprint 音频指纹
     * @return 音乐列表
     */
    @GetMapping("/fingerprint/{fingerprint}")
    public ResponseEntity<Map<String, Object>> getMusicByFingerprint(@PathVariable String fingerprint) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Music> musicList = musicService.findByAudioFingerprint(fingerprint);
            
            List<MusicResponseDto> musicDtos = musicList.stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            response.put("data", musicDtos);
            response.put("count", musicDtos.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 根据流派获取音乐
     * @param genre 流派
     * @param page 页码
     * @param size 每页数量
     * @return 音乐列表
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Map<String, Object>> getMusicByGenre(
            @PathVariable String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Music> musicPage = musicService.findByGenre(genre, pageable);
            
            List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            response.put("content", musicDtos);
            response.put("page", musicPage.getNumber());
            response.put("size", musicPage.getSize());
            response.put("totalElements", musicPage.getTotalElements());
            response.put("totalPages", musicPage.getTotalPages());
            response.put("last", musicPage.isLast());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 根据情绪获取音乐
     * @param mood 情绪
     * @param page 页码
     * @param size 每页数量
     * @return 音乐列表
     */
    @GetMapping("/mood/{mood}")
    public ResponseEntity<Map<String, Object>> getMusicByMood(
            @PathVariable String mood,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Music> musicPage = musicService.findByMood(mood, pageable);
            
            List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            response.put("content", musicDtos);
            response.put("page", musicPage.getNumber());
            response.put("size", musicPage.getSize());
            response.put("totalElements", musicPage.getTotalElements());
            response.put("totalPages", musicPage.getTotalPages());
            response.put("last", musicPage.isLast());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取AI生成的音乐
     * @param page 页码
     * @param size 每页数量
     * @return AI生成的音乐列表
     */
    @GetMapping("/ai-generated")
    public ResponseEntity<Map<String, Object>> getAiGeneratedMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Music> musicPage = musicService.findByAiGenerated(true, pageable);
            
            List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                    .map(this::convertToResponseDto)
                    .collect(Collectors.toList());
            
            response.put("content", musicDtos);
            response.put("page", musicPage.getNumber());
            response.put("size", musicPage.getSize());
            response.put("totalElements", musicPage.getTotalElements());
            response.put("totalPages", musicPage.getTotalPages());
            response.put("last", musicPage.isLast());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 获取音乐统计信息
     * @return 统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMusicStats() {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long totalMusic = musicService.count();
            long aiGeneratedMusic = musicService.countByAiGenerated(true);
            long humanMusic = musicService.countByAiGenerated(false);
            List<String> genres = musicService.findAllGenres();
            List<String> moods = musicService.findAllMoods();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalMusic", totalMusic);
            stats.put("aiGeneratedMusic", aiGeneratedMusic);
            stats.put("humanMusic", humanMusic);
            stats.put("genreCount", genres.size());
            stats.put("moodCount", moods.size());
            stats.put("genres", genres);
            stats.put("moods", moods);
            
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "获取失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}