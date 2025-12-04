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
    public Page<Music> search(String keyword, Pageable pageable) {
        return musicRepository.findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(keyword, keyword, pageable);
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
    public Page<Music> findByGenre(String genre, Pageable pageable) {
        return musicRepository.findByGenre(genre, pageable);
    }
    
    @Override
    public List<Music> findByMood(String mood) {
        return musicRepository.findByMood(mood);
    }
    
    @Override
    public Page<Music> findByMood(String mood, Pageable pageable) {
        return musicRepository.findByMood(mood, pageable);
    }
    
    @Override
    public List<Music> findAiGeneratedMusic() {
        return musicRepository.findByAiGeneratedTrue();
    }
    
    @Override
    public Page<Music> findByAiGenerated(Boolean aiGenerated, Pageable pageable) {
        return musicRepository.findByAiGenerated(aiGenerated, pageable);
    }
    
    @Override
    public Page<Music> findByGenreAndMood(String genre, String mood, Pageable pageable) {
        return musicRepository.findByGenreAndMood(genre, mood, pageable);
    }
    
    @Override
    public Page<Music> findByGenreAndAiGenerated(String genre, Boolean aiGenerated, Pageable pageable) {
        return musicRepository.findByGenreAndAiGenerated(genre, aiGenerated, pageable);
    }
    
    @Override
    public Page<Music> findByMoodAndAiGenerated(String mood, Boolean aiGenerated, Pageable pageable) {
        return musicRepository.findByMoodAndAiGenerated(mood, aiGenerated, pageable);
    }
    
    @Override
    public Page<Music> findByGenreAndMoodAndAiGenerated(String genre, String mood, Boolean aiGenerated, Pageable pageable) {
        return musicRepository.findByGenreAndMoodAndAiGenerated(genre, mood, aiGenerated, pageable);
    }
    
    @Override
    public long count() {
        return musicRepository.count();
    }
    
    @Override
    public long countByAiGenerated(Boolean aiGenerated) {
        return musicRepository.countByAiGenerated(aiGenerated);
    }
    
    @Override
    public List<String> findAllGenres() {
        return musicRepository.findAllGenres();
    }
    
    @Override
    public List<String> findAllMoods() {
        return musicRepository.findAllMoods();
    }
    
    @Override
    public List<String> findAllQualities() {
        return musicRepository.findAllQualities();
    }
}