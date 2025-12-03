package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import com.autospacemusic.repository.MusicRepository;
import com.autospacemusic.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    
    @Autowired
    private MusicRepository musicRepository;
    
    @Override
    public List<Music> recommendForUser(User user, int limit) {
        // 基于用户收藏的音乐推荐相似音乐
        return recommendBasedOnFavorites(user, limit);
    }
    
    @Override
    public List<Music> recommendBasedOnFavorites(User user, int limit) {
        // 这里应该实现基于用户收藏的音乐推荐算法
        // 目前返回随机音乐作为示例
        List<Music> allMusic = musicRepository.findAll();
        return allMusic.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Music> recommendPopular(int limit) {
        // 返回最受欢迎的音乐（按播放次数或其他指标）
        // 目前返回最新的音乐作为示例
        return musicRepository.findAll()
                .stream()
                .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .limit(limit)
                .collect(ArrayList::new, (list, item) -> list.add(0, item), (list1, list2) -> list1.addAll(0, list2));
    }
}