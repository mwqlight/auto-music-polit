package com.autospacemusic.service.impl;

import com.autospacemusic.entity.Playlist;
import com.autospacemusic.repository.PlaylistRepository;
import com.autospacemusic.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {
    
    @Autowired
    private PlaylistRepository playlistRepository;
    
    @Override
    public List<Playlist> findAll() {
        return playlistRepository.findAll();
    }
    
    @Override
    public Optional<Playlist> findById(Long id) {
        return playlistRepository.findById(id);
    }
    
    @Override
    public List<Playlist> findByUserId(Long userId) {
        return playlistRepository.findByUserId(userId);
    }
    
    @Override
    public Playlist save(Playlist playlist) {
        // 设置创建和更新时间
        if (playlist.getId() == null) {
            playlist.setCreatedAt(LocalDateTime.now());
        }
        playlist.setUpdatedAt(LocalDateTime.now());
        
        return playlistRepository.save(playlist);
    }
    
    @Override
    public void deleteById(Long id) {
        playlistRepository.deleteById(id);
    }
}