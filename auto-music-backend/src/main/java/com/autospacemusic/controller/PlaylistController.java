package com.autospacemusic.controller;

import com.autospacemusic.entity.Playlist;
import com.autospacemusic.service.PlaylistService;
import com.autospacemusic.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/playlists")
@CrossOrigin(origins = "*")
public class PlaylistController {
    
    @Autowired
    private PlaylistService playlistService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Playlist>>> getAllPlaylists() {
        List<Playlist> playlists = playlistService.findAll();
        return ResponseEntity.ok(ApiResponse.success(playlists));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Playlist>> getPlaylistById(@PathVariable Long id) {
        Optional<Playlist> playlist = playlistService.findById(id);
        return playlist.map(p -> ResponseEntity.ok(ApiResponse.success(p)))
                .orElse(ResponseEntity.ok(ApiResponse.notFound("播放列表不存在")));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Playlist>>> getPlaylistsByUserId(@PathVariable Long userId) {
        List<Playlist> playlists = playlistService.findByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(playlists));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Playlist>> createPlaylist(@RequestBody Playlist playlist) {
        Playlist savedPlaylist = playlistService.save(playlist);
        return ResponseEntity.ok(ApiResponse.success("播放列表创建成功", savedPlaylist));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Playlist>> updatePlaylist(@PathVariable Long id, @RequestBody Playlist playlist) {
        Optional<Playlist> existingPlaylist = playlistService.findById(id);
        if (existingPlaylist.isPresent()) {
            playlist.setId(id);
            Playlist updatedPlaylist = playlistService.save(playlist);
            return ResponseEntity.ok(ApiResponse.success("播放列表更新成功", updatedPlaylist));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("播放列表不存在"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePlaylist(@PathVariable Long id) {
        Optional<Playlist> playlist = playlistService.findById(id);
        if (playlist.isPresent()) {
            playlistService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("播放列表删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("播放列表不存在"));
        }
    }
}