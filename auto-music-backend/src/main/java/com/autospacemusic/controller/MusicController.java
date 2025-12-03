package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.dto.request.MusicRequestDto;
import com.autospacemusic.dto.response.MusicResponseDto;
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
    public ResponseEntity<List<MusicResponseDto>> getAllMusic(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Music> musicPage = musicService.findAll(pageable);
        List<MusicResponseDto> musicDtos = musicPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(musicDtos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MusicResponseDto> getMusicById(@PathVariable Long id) {
        Optional<Music> music = musicService.findById(id);
        return music.map(m -> ResponseEntity.ok(convertToResponseDto(m)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<MusicResponseDto> createMusic(@RequestBody MusicRequestDto musicDto) {
        Music music = convertToEntity(musicDto);
        Music savedMusic = musicService.save(music);
        MusicResponseDto responseDto = convertToResponseDto(savedMusic);
        return ResponseEntity.ok(responseDto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MusicResponseDto> updateMusic(@PathVariable Long id, @RequestBody MusicRequestDto musicDto) {
        Optional<Music> existingMusic = musicService.findById(id);
        if (existingMusic.isPresent()) {
            Music music = convertToEntity(musicDto);
            music.setId(id);
            Music updatedMusic = musicService.save(music);
            MusicResponseDto responseDto = convertToResponseDto(updatedMusic);
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusic(@PathVariable Long id) {
        Optional<Music> music = musicService.findById(id);
        if (music.isPresent()) {
            musicService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MusicResponseDto>> searchMusic(
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
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/fingerprint/{fingerprint}")
    public ResponseEntity<List<MusicResponseDto>> getMusicByFingerprint(@PathVariable String fingerprint) {
        List<Music> musicList = musicService.findByAudioFingerprint(fingerprint);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MusicResponseDto>> getMusicByGenre(@PathVariable String genre) {
        List<Music> musicList = musicService.findByGenre(genre);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/mood/{mood}")
    public ResponseEntity<List<MusicResponseDto>> getMusicByMood(@PathVariable String mood) {
        List<Music> musicList = musicService.findByMood(mood);
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
    
    @GetMapping("/ai-generated")
    public ResponseEntity<List<MusicResponseDto>> getAiGeneratedMusic() {
        List<Music> musicList = musicService.findAiGeneratedMusic();
        List<MusicResponseDto> responseDtos = musicList.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }
}