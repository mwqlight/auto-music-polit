<template>
  <div class="music-detail" v-if="music">
    <div class="header">
      <h2>{{ music.title }}</h2>
      <p class="artist">by {{ music.artist }}</p>
    </div>
    
    <div class="details-grid">
      <div class="detail-item">
        <span class="label">时长:</span>
        <span class="value">{{ formatDuration(music.durationSeconds) }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">文件路径:</span>
        <span class="value">{{ music.filePath }}</span>
      </div>
      
      <div class="detail-item" v-if="music.audioFingerprint">
        <span class="label">音频指纹:</span>
        <span class="value">{{ music.audioFingerprint }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">AI生成:</span>
        <span class="value" :class="{ 'ai-generated': music.aiGenerated }">
          {{ music.aiGenerated ? '是' : '否' }}
        </span>
      </div>
      
      <div class="detail-item" v-if="music.genre">
        <span class="label">风格:</span>
        <span class="value genre-tag">{{ music.genre }}</span>
      </div>
      
      <div class="detail-item" v-if="music.mood">
        <span class="label">情绪:</span>
        <span class="value mood-tag">{{ music.mood }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">创建时间:</span>
        <span class="value">{{ formatDate(music.createdAt) }}</span>
      </div>
      
      <div class="detail-item">
        <span class="label">更新时间:</span>
        <span class="value">{{ formatDate(music.updatedAt) }}</span>
      </div>
    </div>
    
    <div class="actions">
      <button @click="playMusic" class="play-button">播放</button>
      <button @click="toggleFavorite" class="favorite-button">
        {{ isFavorite ? '取消收藏' : '收藏' }}
      </button>
    </div>
  </div>
  
  <div class="music-detail empty" v-else>
    <p>请选择一首音乐查看详情</p>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useMusicStore } from '@/store/modules/music'
import { useFavoriteStore } from '@/store/modules/favorite'
import { Music } from '@/types/models/music'

const musicStore = useMusicStore()
const favoriteStore = useFavoriteStore()

// 计算属性
const music = computed(() => musicStore.currentMusic)
const isFavorite = computed(() => {
  if (!music.value) return false
  return favoriteStore.favorites.some(fav => fav.musicId === music.value?.id)
})

// 方法
const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

const playMusic = () => {
  if (music.value) {
    musicStore.setCurrentMusic(music.value)
  }
}

const toggleFavorite = () => {
  if (music.value) {
    if (isFavorite.value) {
      favoriteStore.removeFavorite(music.value.id)
    } else {
      favoriteStore.addFavorite(music.value.id)
    }
  }
}
</script>

<style scoped>
.music-detail {
  background: var(--bg-secondary);
  border-radius: 12px;
  border: 1px solid var(--border-color);
  padding: 20px;
  margin: 20px 0;
  box-shadow: var(--shadow-sm);
}

.music-detail.empty {
  text-align: center;
  padding: 40px;
  color: var(--text-secondary);
}

.header {
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 16px;
  margin-bottom: 20px;
}

.header h2 {
  margin: 0 0 8px 0;
  color: var(--primary-color);
  font-size: 24px;
}

.artist {
  margin: 0;
  color: var(--text-secondary);
  font-size: 16px;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  padding: 12px;
  background: var(--bg-tertiary);
  border-radius: 8px;
  border: 1px solid var(--border-color);
  transition: all var(--transition-fast);
}

.detail-item:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-sm);
}

.label {
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: 4px;
  font-size: 14px;
}

.value {
  color: var(--text-secondary);
  font-size: 14px;
}

.ai-generated {
  color: #e74c3c;
  font-weight: 600;
}

.genre-tag, .mood-tag {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 6px;
  color: white;
  font-size: 12px;
  font-weight: 500;
}

.genre-tag {
  background: var(--gradient-primary);
}

.mood-tag {
  background: var(--gradient-secondary);
}

.actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid var(--border-color);
}

.play-button, .favorite-button {
  padding: 10px 20px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all var(--transition-normal);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.play-button {
  background: var(--gradient-primary);
  color: white;
  box-shadow: var(--shadow-md);
}

.favorite-button {
  background: var(--bg-tertiary);
  color: var(--primary-color);
  border: 1px solid var(--border-color);
}

.play-button:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-glow);
}

.favorite-button:hover {
  background: var(--bg-secondary);
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-detail {
    padding: 15px;
    margin: 15px 0;
  }
  
  .header h2 {
    font-size: 20px;
  }
  
  .details-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }
  
  .actions {
    flex-direction: column;
  }
  
  .play-button, .favorite-button {
    width: 100%;
  }
}
</style>