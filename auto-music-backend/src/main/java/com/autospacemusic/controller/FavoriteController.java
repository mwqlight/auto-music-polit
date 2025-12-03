package com.autospacemusic.controller;

import com.autospacemusic.entity.Favorite;
import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import com.autospacemusic.service.FavoriteService;
import com.autospacemusic.service.MusicService;
import com.autospacemusic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/favorites")
@CrossOrigin(origins = "*")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MusicService musicService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getFavoritesByUserId(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.findByUserId(userId);
        return ResponseEntity.ok(favorites);
    }
    
    @PostMapping
    public ResponseEntity<?> addToFavorites(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.badRequest().body("用户或音乐不存在");
        }
        
        try {
            Favorite favorite = favoriteService.addToFavorites(user.get(), music.get());
            return ResponseEntity.ok(favorite);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping
    public ResponseEntity<?> removeFromFavorites(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.badRequest().body("用户或音乐不存在");
        }
        
        favoriteService.removeFromFavorites(user.get(), music.get());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorite(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean isFavorite = favoriteService.isFavorite(user.get(), music.get());
        return ResponseEntity.ok(isFavorite);
    }
}