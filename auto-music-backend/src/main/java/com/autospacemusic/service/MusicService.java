package com.autospacemusic.service;

import com.autospacemusic.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MusicService {
    List<Music> findAll();
    Page<Music> findAll(Pageable pageable);
    Optional<Music> findById(Long id);
    Music save(Music music);
    void deleteById(Long id);
    
    // 搜索相关方法
    List<Music> searchByTitle(String title);
    List<Music> searchByArtist(String artist);
    Page<Music> search(String keyword, Pageable pageable);
    Page<Music> searchByTitleOrArtist(String keyword, Pageable pageable);
    
    // 过滤相关方法
    List<Music> findByAudioFingerprint(String audioFingerprint);
    List<Music> findByGenre(String genre);
    Page<Music> findByGenre(String genre, Pageable pageable);
    List<Music> findByMood(String mood);
    Page<Music> findByMood(String mood, Pageable pageable);
    List<Music> findAiGeneratedMusic();
    Page<Music> findByAiGenerated(Boolean aiGenerated, Pageable pageable);
    
    // 组合过滤方法
    Page<Music> findByGenreAndMood(String genre, String mood, Pageable pageable);
    Page<Music> findByGenreAndAiGenerated(String genre, Boolean aiGenerated, Pageable pageable);
    Page<Music> findByMoodAndAiGenerated(String mood, Boolean aiGenerated, Pageable pageable);
    Page<Music> findByGenreAndMoodAndAiGenerated(String genre, String mood, Boolean aiGenerated, Pageable pageable);
    
    // 统计相关方法
    long count();
    long countByAiGenerated(Boolean aiGenerated);
    List<String> findAllGenres();
    List<String> findAllMoods();
    List<String> findAllQualities();
}