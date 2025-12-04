package com.autospacemusic.controller;

import com.autospacemusic.entity.Favorite;
import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import com.autospacemusic.service.FavoriteService;
import com.autospacemusic.service.MusicService;
import com.autospacemusic.service.UserService;
import com.autospacemusic.dto.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<Favorite>>> getFavoritesByUserId(@PathVariable Long userId) {
        List<Favorite> favorites = favoriteService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(favorites));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Favorite>> addToFavorites(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.ok(ApiResponse.error("用户或音乐不存在"));
        }
        
        try {
            Favorite favorite = favoriteService.addToFavorites(user.get(), music.get());
            return ResponseEntity.ok(ApiResponse.success("添加到收藏夹成功", favorite));
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> removeFromFavorites(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.ok(ApiResponse.error("用户或音乐不存在"));
        }
        
        favoriteService.removeFromFavorites(user.get(), music.get());
        return ResponseEntity.ok(ApiResponse.success("从收藏夹移除成功"));
    }
    
    @GetMapping("/check")
    public ResponseEntity<ApiResponse<Boolean>> isFavorite(@RequestParam Long userId, @RequestParam Long musicId) {
        Optional<User> user = userService.findById(userId);
        Optional<Music> music = musicService.findById(musicId);
        
        if (!user.isPresent() || !music.isPresent()) {
            return ResponseEntity.ok(ApiResponse.error("用户或音乐不存在"));
        }
        
        boolean isFavorite = favoriteService.isFavorite(user.get(), music.get());
        return ResponseEntity.ok(ApiResponse.success(isFavorite));
    }
}