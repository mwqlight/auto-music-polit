package com.autospacemusic.service;

import com.autospacemusic.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);
    void deleteById(Long id);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}