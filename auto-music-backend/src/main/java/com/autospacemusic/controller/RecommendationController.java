package com.autospacemusic.controller;

import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.User;
import com.autospacemusic.service.RecommendationService;
import com.autospacemusic.service.UserService;
import com.autospacemusic.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Music>>> getRecommendationsForUser(@PathVariable Long userId, 
                                                                @RequestParam(defaultValue = "10") int limit) {
        Optional<User> user = userService.findById(userId);
        
        if (!user.isPresent()) {
            return ResponseEntity.ok(ApiResponse.error("用户不存在"));
        }
        
        List<Music> recommendations = recommendationService.recommendForUser(user.get(), limit);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }
    
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<Music>>> getPopularRecommendations(@RequestParam(defaultValue = "10") int limit) {
        List<Music> recommendations = recommendationService.recommendPopular(limit);
        return ResponseEntity.ok(ApiResponse.success(recommendations));
    }
}