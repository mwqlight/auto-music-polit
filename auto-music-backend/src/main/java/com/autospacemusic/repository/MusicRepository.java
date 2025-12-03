package com.autospacemusic.repository;

import com.autospacemusic.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByTitleContainingIgnoreCase(String title);
    List<Music> findByArtistContainingIgnoreCase(String artist);
    List<Music> findByAudioFingerprint(String audioFingerprint);
    List<Music> findByGenre(String genre);
    List<Music> findByMood(String mood);
    List<Music> findByAiGeneratedTrue();
}