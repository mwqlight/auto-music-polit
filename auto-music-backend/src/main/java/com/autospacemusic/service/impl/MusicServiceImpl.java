package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Music;
import com.autospacemusic.repository.MusicRepository;
import com.autospacemusic.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MusicServiceImpl implements MusicService {
    
    @Autowired
    private MusicRepository musicRepository;
    
    @Override
    public List<Music> findAll() {
        return musicRepository.findAll();
    }
    
    @Override
    public Page<Music> findAll(Pageable pageable) {
        return musicRepository.findAll(pageable);
    }
    
    @Override
    public Optional<Music> findById(Long id) {
        return musicRepository.findById(id);
    }
    
    @Override
    public Music save(Music music) {
        // 设置创建和更新时间
        if (music.getId() == null) {
            music.setCreatedAt(LocalDateTime.now());
        }
        music.setUpdatedAt(LocalDateTime.now());
        return musicRepository.save(music);
    }
    
    @Override
    public void deleteById(Long id) {
        musicRepository.deleteById(id);
    }
    
    @Override
    public List<Music> searchByTitle(String title) {
        return musicRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    public List<Music> searchByArtist(String artist) {
        return musicRepository.findByArtistContainingIgnoreCase(artist);
    }
    
    @Override
    public List<Music> findByAudioFingerprint(String audioFingerprint) {
        return musicRepository.findByAudioFingerprint(audioFingerprint);
    }
    
    @Override
    public List<Music> findByGenre(String genre) {
        return musicRepository.findByGenre(genre);
    }
    
    @Override
    public List<Music> findByMood(String mood) {
        return musicRepository.findByMood(mood);
    }
    
    @Override
    public List<Music> findAiGeneratedMusic() {
        return musicRepository.findByAiGeneratedTrue();
    }
}