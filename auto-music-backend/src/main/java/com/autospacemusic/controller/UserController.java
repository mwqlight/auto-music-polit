package com.autospacemusic.controller;

import com.autospacemusic.entity.User;
import com.autospacemusic.service.UserService;
import com.autospacemusic.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success(users));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(u -> ResponseEntity.ok(ApiResponse.success(u)))
                .orElse(ResponseEntity.ok(ApiResponse.notFound("用户不存在")));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userService.existsByUsername(user.getUsername())) {
            return ResponseEntity.ok(ApiResponse.error("用户名已存在"));
        }
        
        // 检查邮箱是否已存在
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.ok(ApiResponse.error("邮箱已存在"));
        }
        
        User savedUser = userService.save(user);
        return ResponseEntity.ok(ApiResponse.success(savedUser));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = userService.findById(id);
        if (existingUser.isPresent()) {
            user.setId(id);
            User updatedUser = userService.save(user);
            return ResponseEntity.ok(ApiResponse.success(updatedUser));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("用户不存在"));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("删除成功"));
        } else {
            return ResponseEntity.ok(ApiResponse.notFound("用户不存在"));
        }
    }
}