package com.autospacemusic.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "music")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String artist;
    
    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "audio_fingerprint", length = 64)
    private String audioFingerprint;
    
    @Column(name = "ai_generated")
    private Boolean aiGenerated = false;
    
    @Column(name = "genre")
    private String genre;
    
    @Column(name = "mood")
    private String mood;
    
    @Column(name = "quality")
    private String quality;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getArtist() {
        return artist;
    }
    
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Integer getDurationSeconds() {
        return durationSeconds;
    }
    
    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
    
    public String getAudioFingerprint() {
        return audioFingerprint;
    }
    
    public void setAudioFingerprint(String audioFingerprint) {
        this.audioFingerprint = audioFingerprint;
    }
    
    public Boolean getAiGenerated() {
        return aiGenerated;
    }
    
    public void setAiGenerated(Boolean aiGenerated) {
        this.aiGenerated = aiGenerated;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getMood() {
        return mood;
    }
    
    public void setMood(String mood) {
        this.mood = mood;
    }
    
    public String getQuality() {
        return quality;
    }
    
    public void setQuality(String quality) {
        this.quality = quality;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}