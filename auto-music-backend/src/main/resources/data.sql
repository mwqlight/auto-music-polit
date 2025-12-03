-- Insert sample data
INSERT INTO users (username, email, password) VALUES 
('admin', 'admin@example.com', '$2a$10$8K1p/a0dhrxiowP.dnkgNORTWgdEDHn5L2/xjpEWuC.QQv4rKO9jO'), -- password: admin123
('user1', 'user1@example.com', '$2a$10$8K1p/a0dhrxiowP.dnkgNORTWgdEDHn5L2/xjpEWuC.QQv4rKO9jO'); -- password: admin123

INSERT INTO musics (title, artist, file_path, duration_seconds, audio_fingerprint, ai_generated, genre, mood) VALUES 
('Starlight Symphony', 'Cosmic Waves', '/music/starlight_symphony.mp3', 240, 'abc123def456', false, 'Electronic', 'Epic'),
('Digital Dreams', 'Neon Pulse', '/music/digital_dreams.mp3', 180, 'def456ghi789', false, 'Synthwave', 'Energetic'),
('Echoes of Time', 'Ancient Melodies', '/music/echoes_of_time.mp3', 300, 'ghi789jkl012', true, 'Ambient', 'Reflective');

INSERT INTO playlists (name, description, user_id) VALUES 
('My Favorites', 'Collection of my favorite tracks', 1),
('Workout Mix', 'Energetic songs for exercise', 1);

INSERT INTO playlist_music (playlist_id, music_id) VALUES 
(1, 1),
(1, 2),
(2, 3);

INSERT INTO favorites (user_id, music_id) VALUES 
(1, 1),
(1, 2);