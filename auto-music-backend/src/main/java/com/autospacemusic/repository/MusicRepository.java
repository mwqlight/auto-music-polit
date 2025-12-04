package com.autospacemusic.repository;

import com.autospacemusic.entity.Music;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByTitleContainingIgnoreCase(String title);
    List<Music> findByArtistContainingIgnoreCase(String artist);
    Page<Music> findByTitleContainingIgnoreCaseOrArtistContainingIgnoreCase(String title, String artist, Pageable pageable);
    List<Music> findByAudioFingerprint(String audioFingerprint);
    List<Music> findByGenre(String genre);
    Page<Music> findByGenre(String genre, Pageable pageable);
    List<Music> findByMood(String mood);
    Page<Music> findByMood(String mood, Pageable pageable);
    List<Music> findByAiGeneratedTrue();
    Page<Music> findByAiGenerated(Boolean aiGenerated, Pageable pageable);
    Page<Music> findByGenreAndMood(String genre, String mood, Pageable pageable);
    Page<Music> findByGenreAndAiGenerated(String genre, Boolean aiGenerated, Pageable pageable);
    Page<Music> findByMoodAndAiGenerated(String mood, Boolean aiGenerated, Pageable pageable);
    Page<Music> findByGenreAndMoodAndAiGenerated(String genre, String mood, Boolean aiGenerated, Pageable pageable);
    long countByAiGenerated(Boolean aiGenerated);
    
    @Query("SELECT DISTINCT m.genre FROM Music m WHERE m.genre IS NOT NULL")
    List<String> findAllGenres();
    
    @Query("SELECT DISTINCT m.mood FROM Music m WHERE m.mood IS NOT NULL")
    List<String> findAllMoods();
    
    @Query("SELECT DISTINCT m.quality FROM Music m WHERE m.quality IS NOT NULL")
    List<String> findAllQualities();
}