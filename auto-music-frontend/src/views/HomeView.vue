<script setup lang="ts">
import { onMounted } from 'vue'
import MusicList from '@/components/audio/MusicList.vue'
import MusicDetail from '@/components/audio/MusicDetail.vue'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'

// 创建粒子效果
const createParticles = () => {
  const particlesBg = document.querySelector('.particles-bg') as HTMLElement
  if (!particlesBg) return
  
  const particleCount = 100
  for (let i = 0; i < particleCount; i++) {
    const particle = document.createElement('div')
    particle.className = 'particle'
    particle.style.left = Math.random() * 100 + '%'
    particle.style.animationDelay = Math.random() * 10 + 's'
    particle.style.animationDuration = (Math.random() * 10 + 10) + 's'
    particlesBg.appendChild(particle)
  }
}

onMounted(() => {
  createParticles()
})
</script>

<template>
  <div class="home">
    <!-- 粒子效果背景 -->
    <div class="particles-bg"></div>
    
    <header class="header">
      <h1 class="high-tech-title">星韵音域 - 音乐智能驾驶舱</h1>
      <p class="high-tech-subtitle">欢迎来到您的个人音乐宇宙</p>
    </header>
    
    <!-- 核心功能按钮区域 -->
    <section class="feature-buttons">
      <div class="button-grid">
        <button class="high-tech-button" @click="$router.push('/music-library')">
          🎵 音乐库
        </button>
        <button class="high-tech-button" @click="$router.push('/ai-music')">
          🤖 AI音乐生成
        </button>
        <button class="high-tech-button" @click="$router.push('/playlists')">
          📋 播放列表
        </button>
        <button class="high-tech-button" @click="$router.push('/favorites')">
          ❤️ 我的收藏
        </button>
        <button class="high-tech-button" @click="$router.push('/music-crawler')">
          🕷️ 音乐源爬取
        </button>
        <button class="high-tech-button" @click="$router.push('/recommendations')">
          🔍 智能推荐
        </button>
      </div>
    </section>
    
    <main class="main-content">
      <div class="content-wrapper">
        <div class="high-tech-card">
          <h2 class="card-title">热门音乐</h2>
          <MusicList />
        </div>
        <div class="high-tech-card">
          <h2 class="card-title">音乐详情</h2>
          <MusicDetail />
        </div>
      </div>
      <AudioPlayer />
    </main>
  </div>
</template>

<style scoped>
.home {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
}

.header {
  text-align: center;
  margin-bottom: 50px;
  padding: 40px 20px;
}

.feature-buttons {
  margin-bottom: 50px;
}

.button-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.card-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 24px;
  font-weight: 700;
  color: #00ffff;
  margin-bottom: 20px;
  text-align: center;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

@media (max-width: 768px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }
  
  .button-grid {
    grid-template-columns: 1fr;
  }
}
</style>