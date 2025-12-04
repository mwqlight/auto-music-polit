package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.dto.request.MusicRequestDto;
import com.autospacemusic.dto.response.MusicResponseDto;
import com.autospacemusic.dto.response.ApiResponse;
import com.autospacemusic.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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
        return music;
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> getAllMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Music> musicPage = musicService.findAll(pageable);
        List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(musicDtos));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MusicResponseDto>> getMusicById(@PathVariable Long id) {
        Optional<Music> music = musicService.findById(id);
        return music.map(m -> ResponseEntity.ok(ApiResponse.success(convertToResponseDto(m))))
                .orElse(ResponseEntity.ok(ApiResponse.notFound("音乐不存在")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<MusicResponseDto>> createMusic(@RequestBody MusicRequestDto musicDto) {
        Music music = convertToEntity(musicDto);
        Music savedMusic = musicService.save(music);
        MusicResponseDto responseDto = convertToResponseDto(savedMusic);
        return ResponseEntity.ok(ApiResponse.success("音乐创建成功", responseDto));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MusicResponseDto>> updateMusic(@PathVariable Long id, @RequestBody MusicRequestDto musicDto) {
        Optional<Music> existingMusic = musicService.findById(id);
        if (existingMusic.isPresent()) {
            Music music = convertToEntity(musicDto);
            music.setId(id);
            Music updatedMusic = musicService.save(music);
            MusicResponseDto responseDto = convertToResponseDto(updatedMusic);
            return ResponseEntity.ok(ApiResponse.success("音乐更新成功", responseDto));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("音乐不存在"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMusic(@PathVariable Long id) {
        Optional<Music> music = musicService.findById(id);
        if (music.isPresent()) {
            musicService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("音乐删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("音乐不存在"));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> searchMusic(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String artist) {
        List<Music> results;
        if (title != null && !title.isEmpty()) {
            results = musicService.searchByTitle(title);
        } else if (artist != null && !artist.isEmpty()) {
            results = musicService.searchByArtist(artist);
        } else {
            results = musicService.findAll();
        }
        List<MusicResponseDto> responseDtos = results.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }
    
    @GetMapping("/fingerprint/{fingerprint}")
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> getMusicByFingerprint(@PathVariable String fingerprint) {
        List<Music> musicList = musicService.findByAudioFingerprint(fingerprint);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> getMusicByGenre(@PathVariable String genre) {
        List<Music> musicList = musicService.findByGenre(genre);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }
    
    @GetMapping("/mood/{mood}")
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> getMusicByMood(@PathVariable String mood) {
        List<Music> musicList = musicService.findByMood(mood);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }
    
    @GetMapping("/ai-generated")
    public ResponseEntity<ApiResponse<List<MusicResponseDto>>> getAiGeneratedMusic() {
        List<Music> musicList = musicService.findAiGeneratedMusic();
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }
}