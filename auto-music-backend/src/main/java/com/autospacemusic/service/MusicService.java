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
    List<Music> searchByTitle(String title);
    List<Music> searchByArtist(String artist);
    List<Music> findByAudioFingerprint(String audioFingerprint);
    List<Music> findByGenre(String genre);
    List<Music> findByMood(String mood);
    List<Music> findAiGeneratedMusic();
}