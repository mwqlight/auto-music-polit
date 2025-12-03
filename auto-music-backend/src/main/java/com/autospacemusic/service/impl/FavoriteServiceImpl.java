package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Favorite;
import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import com.autospacemusic.repository.FavoriteRepository;
import com.autospacemusic.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteServiceImpl implements FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    @Override
    public List<Favorite> findByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }
    
    @Override
    public Favorite addToFavorites(User user, Music music) {
        // 检查是否已经收藏
        if (favoriteRepository.existsByUserAndMusic(user, music)) {
            throw new RuntimeException("音乐已在收藏夹中");
        }
        
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setMusic(music);
        favorite.setCreatedAt(LocalDateTime.now());
        
        return favoriteRepository.save(favorite);
    }
    
    @Override
    public void removeFromFavorites(User user, Music music) {
        favoriteRepository.deleteByUserAndMusic(user, music);
    }
    
    @Override
    public boolean isFavorite(User user, Music music) {
        return favoriteRepository.existsByUserAndMusic(user, music);
    }
    
    @Override
    public void deleteById(Long id) {
        favoriteRepository.deleteById(id);
    }
}