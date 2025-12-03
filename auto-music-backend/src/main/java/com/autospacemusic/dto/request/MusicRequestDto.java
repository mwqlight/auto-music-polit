package com.autospacemusic.dto.request;

public class MusicRequestDto {
    private String title;
    private String artist;
    private String filePath;
    private Integer durationSeconds;
    private String audioFingerprint;
    private Boolean aiGenerated;
    private String genre;
    private String mood;
    
    // Getters and setters
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
}