package com.autospacemusic.service;

public interface AiMusicService {
    String recognizeMusic(byte[] audioData);
    byte[] generateMusic(String style, String mood, int duration);
    byte[] remixMusic(byte[] originalMusic, String style);
}