package com.autospacemusic.config;

import com.autospacemusic.entity.Music;
import com.autospacemusic.entity.Playlist;
import com.autospacemusic.entity.User;
import com.autospacemusic.repository.MusicRepository;
import com.autospacemusic.repository.PlaylistRepository;
import com.autospacemusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否已有数据，如果没有则初始化
        if (musicRepository.count() == 0) {
            initializeMusicData();
            initializeUserData();
            initializePlaylistData();
        }
    }

    private void initializeMusicData() {
        List<Music> musicList = new ArrayList<>();

        // 流行音乐
        musicList.add(createMusic(
            "Blinding Lights",
            "The Weeknd",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            200,
            "pop",
            "energetic",
            "high",
            false
        ));

        musicList.add(createMusic(
            "Shape of You",
            "Ed Sheeran",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            233,
            "pop",
            "dance",
            "high",
            false
        ));

        // 摇滚音乐
        musicList.add(createMusic(
            "Bohemian Rhapsody",
            "Queen",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3",
            354,
            "rock",
            "epic",
            "high",
            false
        ));

        musicList.add(createMusic(
            "Stairway to Heaven",
            "Led Zeppelin",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            482,
            "rock",
            "progressive",
            "high",
            false
        ));

        // 古典音乐
        musicList.add(createMusic(
            "Moonlight Sonata",
            "Ludwig van Beethoven",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3",
            325,
            "classical",
            "calm",
            "high",
            false
        ));

        musicList.add(createMusic(
            "Für Elise",
            "Ludwig van Beethoven",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-6.mp3",
            140,
            "classical",
            "romantic",
            "high",
            false
        ));

        // AI生成音乐
        musicList.add(createMusic(
            "Neural Symphony",
            "AI Composer",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            180,
            "electronic",
            "futuristic",
            "high",
            true
        ));

        musicList.add(createMusic(
            "Digital Dreams",
            "AI Artist",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-8.mp3",
            210,
            "electronic",
            "ambient",
            "high",
            true
        ));

        musicList.add(createMusic(
            "Cyberpunk Nights",
            "AI Music Generator",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-9.mp3",
            240,
            "electronic",
            "energetic",
            "high",
            true
        ));

        musicList.add(createMusic(
            "Quantum Melodies",
            "AI Composer X",
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3",
            195,
            "electronic",
            "mysterious",
            "high",
            true
        ));

        musicRepository.saveAll(musicList);
        System.out.println("Initialized " + musicList.size() + " music records");
    }

    private void initializeUserData() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi"); // password: admin
        adminUser.setEmail("admin@example.com");
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi"); // password: admin
        testUser.setEmail("test@example.com");
        testUser.setCreatedAt(LocalDateTime.now());
        testUser.setUpdatedAt(LocalDateTime.now());

        userRepository.saveAll(List.of(adminUser, testUser));
        System.out.println("Initialized 2 user records");
    }

    private void initializePlaylistData() {
        // 获取所有音乐
        List<Music> allMusic = musicRepository.findAll();

        // 获取管理员用户
        User adminUser = userRepository.findByUsername("admin").orElseThrow();

        // 创建默认播放列表
        Playlist favorites = new Playlist();
        favorites.setName("我的收藏");
        favorites.setDescription("我最喜欢的音乐");
        favorites.setUser(adminUser);
        favorites.setMusics(allMusic.subList(0, 3)); // 收藏前3首歌
        favorites.setCreatedAt(LocalDateTime.now());
        favorites.setUpdatedAt(LocalDateTime.now());

        Playlist aiMusic = new Playlist();
        aiMusic.setName("AI音乐精选");
        aiMusic.setDescription("AI生成的优质音乐");
        aiMusic.setUser(adminUser);
        aiMusic.setMusics(allMusic.subList(6, 10)); // AI生成的音乐
        aiMusic.setCreatedAt(LocalDateTime.now());
        aiMusic.setUpdatedAt(LocalDateTime.now());

        Playlist workout = new Playlist();
        workout.setName("运动健身");
        workout.setDescription("充满活力的运动音乐");
        workout.setUser(adminUser);
        workout.setMusics(allMusic.subList(0, 2)); // 动感音乐
        workout.setCreatedAt(LocalDateTime.now());
        workout.setUpdatedAt(LocalDateTime.now());

        playlistRepository.saveAll(List.of(favorites, aiMusic, workout));
        System.out.println("Initialized 3 playlist records");
    }

    private Music createMusic(String title, String artist, String filePath, int durationSeconds, 
                            String genre, String mood, String quality, boolean aiGenerated) {
        Music music = new Music();
        music.setTitle(title);
        music.setArtist(artist);
        music.setFilePath(filePath);
        music.setDurationSeconds(durationSeconds);
        music.setGenre(genre);
        music.setMood(mood);
        music.setQuality(quality);
        music.setAiGenerated(aiGenerated);
        music.setAudioFingerprint(generateAudioFingerprint(title, artist));
        music.setCreatedAt(LocalDateTime.now());
        music.setUpdatedAt(LocalDateTime.now());
        return music;
    }

    private String generateAudioFingerprint(String title, String artist) {
        // 简单生成音频指纹（实际项目中应该使用专业的音频指纹算法）
        return Integer.toHexString((title + artist).hashCode());
    }
}