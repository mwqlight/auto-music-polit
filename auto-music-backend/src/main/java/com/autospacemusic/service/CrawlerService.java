package com.autospacemusic.service;

import com.autospacemusic.entity.Music;
import java.io.File;
import java.util.List;
import java.util.Map;

public interface CrawlerService {
    // 从指定源URL爬取音乐
    List<Music> crawlMusicFromSource(String sourceUrl, String sourceType);
    
    // 支持多源同时爬取
    Map<String, List<Music>> crawlMusicFromMultipleSources(Map<String, String> sourceMap);
    
    // 智能内容识别和分类
    void analyzeAndClassifyMusic(Music music);
    
    // 定时任务调度
    void scheduleCrawlingJob(String cronExpression);
    
    // 获取支持的音乐源类型
    List<String> getSupportedSourceTypes();
    
    // 爬取所有支持的音乐源
    void crawlAllSupportedSources();
    
    // 从本地视频文件提取音乐
    Music extractMusicFromVideoFile(File videoFile);
    
    // 从视频链接提取音乐
    Music extractMusicFromVideoUrl(String videoUrl);
    
    // 音乐搜索功能
    List<Music> searchMusic(String keyword);
    
    // 音频剪切功能
    File cutAudio(File audioFile, int startTime, int endTime);
    
    // 音频合并功能
    File mergeAudios(List<File> audioFiles);
    
    // 添加音效功能
    File addAudioEffect(File audioFile, String effectType);
}