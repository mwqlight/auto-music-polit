package com.autospacemusic.service;

import com.autospacemusic.entity.Favorite;
import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {
    List<Favorite> findByUserId(Long userId);
    Favorite addToFavorites(User user, Music music);
    void removeFromFavorites(User user, Music music);
    boolean isFavorite(User user, Music music);
    void deleteById(Long id);
}