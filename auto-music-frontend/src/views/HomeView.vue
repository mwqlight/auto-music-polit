<script setup lang="ts">
import { useRouter } from 'vue-router'
import MusicList from '@/components/audio/MusicList.vue'
import MusicDetail from '@/components/audio/MusicDetail.vue'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'

const router = useRouter()

// 核心功能列表
const features = [
  {
    id: 'music-library',
    title: '音乐库',
    description: '浏览和管理您的音乐收藏',
    icon: '🎵',
    route: '/music'
  },
  {
    id: 'playlists',
    title: '播放列表',
    description: '创建和管理个性化播放列表',
    icon: '📋',
    route: '/playlists'
  },
  {
    id: 'favorites',
    title: '我的收藏',
    description: '查看和管理您收藏的音乐',
    icon: '❤️',
    route: '/favorites'
  },
  {
    id: 'ai-music',
    title: 'AI音乐',
    description: 'AI驱动的音乐创作与识别',
    icon: '🤖',
    route: '/ai-music'
  },
  {
    id: 'profile',
    title: '个人中心',
    description: '管理您的个人信息和设置',
    icon: '👤',
    route: '/profile'
  }
]
</script>

<template>
  <div class="home">
    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>星韵音域 - 音乐智能驾驶舱</h1>
        <p class="hero-subtitle">欢迎来到您的个人音乐宇宙</p>
        <p class="hero-description">
          一个革命性的个人音乐生态系统，将分散在全网的音频资源统一整合为个人专属的智能音乐库。
          通过先进的爬虫技术从各种开源资源中提取音乐内容，利用AI实现听音识曲、智能编曲等功能。
        </p>
      </div>
    </section>

    <!-- 核心功能区域 -->
    <section class="features-section">
      <h2>核心功能</h2>
      <div class="features-grid">
        <div 
          v-for="feature in features" 
          :key="feature.id"
          class="feature-card card"
          @click="router.push(feature.route)"
        >
          <div class="feature-icon">
            {{ feature.icon }}
          </div>
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
          <button class="btn btn-primary">
            立即体验
          </button>
        </div>
      </div>
    </section>

    <!-- 热门音乐区域 -->
    <section class="music-section">
      <h2>热门音乐</h2>
      <div class="music-wrapper">
        <MusicList />
      </div>
    </section>

    <!-- 音频播放器 -->
    <AudioPlayer />
  </div>
</template>

<style scoped>
.home-container {
  min-height: 100vh;
  background: var(--bg-primary);
  padding: var(--spacing-xxl) var(--spacing-lg);
  position: relative;
  overflow: hidden;
}

/* 背景网格动画 */
.home-container::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 200%;
  height: 200%;
  background-image: 
    linear-gradient(rgba(0, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  animation: gridMove 20s linear infinite;
  z-index: 0;
  pointer-events: none;
}

@keyframes gridMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

.hero-section {
  text-align: center;
  margin-bottom: var(--spacing-xxxl);
  padding: var(--spacing-xxxl) var(--spacing-lg);
  background: var(--gradient-primary);
  border-radius: var(--radius-xxl);
  color: white;
  box-shadow: var(--shadow-double-glow);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 1s ease-out;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  animation: shimmer 3s ease-in-out infinite;
}

@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.hero-content h1 {
  font-size: clamp(3rem, 8vw, 4rem);
  font-weight: 800;
  margin-bottom: var(--spacing-xl);
  background: linear-gradient(135deg, #ffffff, #e0e7ff, #a78bfa);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 40px rgba(255, 255, 255, 0.4);
  animation: titleGlow 3s ease-in-out infinite;
}

@keyframes titleGlow {
  0%, 100% { text-shadow: 0 0 40px rgba(255, 255, 255, 0.4); }
  50% { text-shadow: 0 0 60px rgba(255, 255, 255, 0.6); }
}

.hero-subtitle {
  font-size: clamp(1rem, 2.5vw, 1.5rem);
  opacity: 0.95;
  margin-bottom: var(--spacing-xxl);
  text-shadow: 0 0 20px rgba(255, 255, 255, 0.3);
}

.hero-description {
  font-size: clamp(0.875rem, 2vw, 1rem);
  color: rgba(255, 255, 255, 0.9);
  max-width: 800px;
  margin: 0 auto;
  line-height: 1.8;
  text-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
}

/* 核心功能区域 */
.features-section {
  margin-bottom: var(--spacing-xxxl);
  position: relative;
  z-index: 1;
}

.features-section h2 {
  font-size: clamp(2rem, 5vw, 2.5rem);
  font-weight: 700;
  color: var(--primary-color);
  text-align: center;
  margin-bottom: var(--spacing-xxl);
  text-shadow: 0 0 20px var(--primary-glow);
  animation: fadeInUp 1s ease-out 0.2s both;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--spacing-xxl);
  max-width: 1400px;
  margin: 0 auto;
}

.feature-card {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xxl);
  text-align: center;
  border: 1px solid var(--border-color);
  transition: all var(--transition-smoother);
  box-shadow: var(--shadow-xl);
  position: relative;
  overflow: hidden;
  animation: fadeInUp 1s ease-out both;
}

.feature-card:nth-child(1) { animation-delay: 0.3s; }
.feature-card:nth-child(2) { animation-delay: 0.4s; }
.feature-card:nth-child(3) { animation-delay: 0.5s; }
.feature-card:nth-child(4) { animation-delay: 0.6s; }
.feature-card:nth-child(5) { animation-delay: 0.7s; }

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transition: left var(--transition-smoother);
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-12px) scale(1.02);
  border-color: var(--primary-color);
  box-shadow: var(--shadow-double-glow);
}

.feature-icon {
  font-size: clamp(2.5rem, 6vw, 3rem);
  margin-bottom: var(--spacing-xl);
  color: var(--primary-color);
  text-shadow: 0 0 20px var(--primary-glow);
  transition: all var(--transition-smoother);
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
  color: var(--primary-light);
  text-shadow: 0 0 30px var(--primary-glow);
}

.feature-card h3 {
  font-size: clamp(1.25rem, 3vw, 1.5rem);
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  color: var(--text-primary);
  transition: all var(--transition-normal);
}

.feature-card:hover h3 {
  color: var(--primary-light);
}

.feature-card p {
  font-size: clamp(0.875rem, 2vw, 1rem);
  color: var(--text-secondary);
  line-height: 1.8;
  transition: all var(--transition-normal);
}

.feature-card:hover p {
  color: var(--text-primary);
}

/* 热门音乐区域 */
.music-section {
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  animation: fadeInUp 1s ease-out 0.8s both;
}

.music-section h2 {
  font-size: clamp(2rem, 5vw, 2.5rem);
  font-weight: 700;
  color: var(--primary-color);
  text-align: center;
  margin-bottom: var(--spacing-xxl);
  text-shadow: 0 0 20px var(--primary-glow);
}

.music-wrapper {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-color);
  padding: var(--spacing-xxl);
  box-shadow: var(--shadow-xl);
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .features-grid {
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: var(--spacing-xl);
  }
}

@media (max-width: 768px) {
  .home-container {
    padding: var(--spacing-xl) var(--spacing-md);
  }
  
  .hero-section {
    padding: var(--spacing-xxl) var(--spacing-md);
    margin-bottom: var(--spacing-xxl);
  }
  
  .features-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-lg);
  }
  
  .feature-card {
    padding: var(--spacing-xl);
  }
  
  .music-wrapper {
    padding: var(--spacing-xl);
  }
}

@media (max-width: 480px) {
  .home-container {
    padding: var(--spacing-lg) var(--spacing-sm);
  }
  
  .hero-section {
    padding: var(--spacing-xl) var(--spacing-sm);
    margin-bottom: var(--spacing-xl);
  }
  
  .feature-card {
    padding: var(--spacing-lg);
  }
  
  .music-wrapper {
    padding: var(--spacing-lg);
  }
}
</style>