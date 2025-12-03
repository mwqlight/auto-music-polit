package com.autospacemusic.service;

import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;

import java.util.List;

public interface RecommendationService {
    List<Music> recommendForUser(User user, int limit);
    List<Music> recommendBasedOnFavorites(User user, int limit);
    List<Music> recommendPopular(int limit);
}