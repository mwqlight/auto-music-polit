<script setup lang="ts">
import { useRouter } from 'vue-router'
import MusicList from '@/components/audio/MusicList.vue'
import MusicDetail from '@/components/audio/MusicDetail.vue'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'

const router = useRouter()

const coreFeatures = [
  { name: '音乐采集中枢', icon: '🎵', route: '/music-library' },
  { name: '量子音乐库', icon: '📚', route: '/music-library' },
  { name: 'AI音乐中枢', icon: '🤖', route: '/ai-music' },
  { name: '智能播放列表', icon: '📋', route: '/playlists' },
  { name: '收藏夹', icon: '❤️', route: '/favorites' },
  { name: '个人中心', icon: '👤', route: '/profile' },
]
</script>

<template>
  <div class="home">
    <!-- 头部区域 -->
    <header class="header">
      <div class="header-content">
        <h1 class="main-title">
          <span class="title-icon">🎵</span>
          星韵音域
          <span class="title-sub">音乐智能驾驶舱</span>
        </h1>
        <p class="subtitle">欢迎来到您的个人音乐宇宙</p>
      </div>
    </header>
    
    <!-- 核心功能区域 -->
    <section class="feature-section">
      <h2 class="section-title">核心功能</h2>
      <div class="feature-grid">
        <div 
          v-for="feature in coreFeatures" 
          :key="feature.name"
          class="feature-card"
          @click="router.push(feature.route)"
        >
          <div class="feature-icon">{{ feature.icon }}</div>
          <h3 class="feature-name">{{ feature.name }}</h3>
        </div>
      </div>
    </section>
    
    <!-- 音乐播放区域 -->
    <main class="main-content">
      <h2 class="section-title">推荐音乐</h2>
      <div class="content-wrapper">
        <MusicList />
        <MusicDetail />
      </div>
    </main>
    
    <!-- 音频播放器 -->
    <AudioPlayer />
  </div>
</template>

<style scoped>
.home {
  padding: 20px;
  max-width: 1400px;
  margin: 0 auto;
  min-height: 100vh;
}

/* 头部样式 */
.header {
  text-align: center;
  margin-bottom: 60px;
  padding: 40px 20px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 153, 255, 0.05) 100%);
  border-radius: 20px;
  border: 1px solid rgba(0, 212, 255, 0.2);
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.1);
}

.main-title {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 15px;
  background: linear-gradient(180deg, #00d4ff 0%, #0099ff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.title-icon {
  font-size: 56px;
  margin-right: 15px;
}

.title-sub {
  font-size: 24px;
  font-weight: 400;
  margin-left: 15px;
  opacity: 0.8;
}

.subtitle {
  font-size: 20px;
  color: #a0a0a0;
  font-weight: 300;
}

/* 功能区域样式 */
.feature-section {
  margin-bottom: 60px;
}

.section-title {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 40px;
  text-align: center;
  color: #00d4ff;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 30px;
}

.feature-card {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.05) 0%, rgba(0, 153, 255, 0.03) 100%);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 15px;
  padding: 30px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.05);
}

.feature-card:hover {
  transform: translateY(-10px);
  border-color: rgba(0, 212, 255, 0.5);
  box-shadow: 0 10px 40px rgba(0, 212, 255, 0.2);
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 153, 255, 0.08) 100%);
}

.feature-icon {
  font-size: 48px;
  margin-bottom: 15px;
}

.feature-name {
  font-size: 20px;
  font-weight: 500;
  color: #e0e0e0;
}

/* 音乐播放区域样式 */
.main-content {
  margin-bottom: 40px;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 30px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .feature-grid {
    grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .home {
    padding: 15px;
  }
  
  .header {
    margin-bottom: 40px;
    padding: 30px 15px;
  }
  
  .main-title {
    font-size: 32px;
  }
  
  .title-icon {
    font-size: 40px;
    margin-right: 10px;
  }
  
  .title-sub {
    font-size: 16px;
    margin-left: 10px;
  }
  
  .subtitle {
    font-size: 16px;
  }
  
  .section-title {
    font-size: 24px;
    margin-bottom: 30px;
  }
  
  .feature-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
  }
  
  .feature-card {
    padding: 20px 15px;
  }
  
  .feature-icon {
    font-size: 36px;
  }
  
  .feature-name {
    font-size: 16px;
  }
  
  .content-wrapper {
    grid-template-columns: 1fr;
    gap: 20px;
  }
}
</style>