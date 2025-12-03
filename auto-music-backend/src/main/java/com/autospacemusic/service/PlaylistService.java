package com.autospacemusic.service;

import com.autospacemusic.entity.Playlist;

import java.util.List;
import java.util.Optional;

public interface PlaylistService {
    List<Playlist> findAll();
    Optional<Playlist> findById(Long id);
    List<Playlist> findByUserId(Long userId);
    Playlist save(Playlist playlist);
    void deleteById(Long id);
}