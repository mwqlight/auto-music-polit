package com.autospacemusic.repository;

import com.autospacemusic.entity.Favorite;
import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserAndMusic(User user, Music music);
    boolean existsByUserAndMusic(User user, Music music);
    void deleteByUserAndMusic(User user, Music music);
}