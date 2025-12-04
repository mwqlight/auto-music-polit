<script setup lang="ts">
import { ref, onMounted } from 'vue'

const recommendations = ref<any[]>([])
const loading = ref(true)

const fetchRecommendations = () => {
  loading.value = true
  
  // 模拟获取推荐音乐
  setTimeout(() => {
    recommendations.value = [
      { id: 1, title: '智能推荐音乐1', artist: '推荐艺术家A', genre: '流行', mood: '欢快' },
      { id: 2, title: '智能推荐音乐2', artist: '推荐艺术家B', genre: '摇滚', mood: '激情' },
      { id: 3, title: '智能推荐音乐3', artist: '推荐艺术家C', genre: '古典', mood: '放松' },
      { id: 4, title: '智能推荐音乐4', artist: '推荐艺术家D', genre: '爵士', mood: '优雅' },
      { id: 5, title: '智能推荐音乐5', artist: '推荐艺术家E', genre: '电子', mood: '动感' }
    ]
    loading.value = false
  }, 1500)
}

onMounted(() => {
  fetchRecommendations()
})
</script>

<template>
  <div class="recommendations">
    <h1 class="high-tech-title">智能推荐</h1>
    
    <div class="high-tech-card">
      <div class="loading-section" v-if="loading">
        <p class="loading-text">正在为您生成个性化推荐...</p>
      </div>
      
      <div class="recommendations-grid" v-else>
        <div class="recommendation-card" v-for="music in recommendations" :key="music.id">
          <div class="music-info">
            <h3>{{ music.title }}</h3>
            <p>{{ music.artist }}</p>
            <div class="music-tags">
              <span class="tag">{{ music.genre }}</span>
              <span class="tag">{{ music.mood }}</span>
            </div>
          </div>
          <button class="high-tech-button small">
            ▶️ 播放
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.recommendations {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.loading-section {
  text-align: center;
  padding: 50px 0;
}

.loading-text {
  color: #00ffff;
  font-size: 20px;
  font-weight: 600;
}

.recommendations-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.recommendation-card {
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 20px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.recommendation-card:hover {
  border-color: rgba(0, 255, 255, 0.5);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.2);
  transform: translateY(-5px);
}

.music-info h3 {
  color: #00ffff;
  font-size: 20px;
  margin-bottom: 5px;
}

.music-info p {
  color: rgba(0, 255, 255, 0.7);
  font-size: 16px;
  margin-bottom: 10px;
}

.music-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.tag {
  padding: 4px 12px;
  background: rgba(0, 255, 255, 0.2);
  border-radius: 20px;
  color: #00ffff;
  font-size: 12px;
  font-weight: 600;
}

.small {
  padding: 8px 20px;
  font-size: 14px;
  align-self: flex-start;
}
</style>