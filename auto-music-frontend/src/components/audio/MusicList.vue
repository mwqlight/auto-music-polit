<template>
  <div class="music-list">
    <div class="list-header">
      <h2>音乐库</h2>
      <div class="search-box">
        <input 
          type="text" 
          v-model="searchKeyword" 
          placeholder="搜索音乐..." 
          @input="handleSearch"
        />
      </div>
    </div>
    <div class="list-container">
      <div 
        v-for="music in musicList" 
        :key="music.id" 
        class="music-item"
        @click="selectMusic(music)"
      >
        <div class="music-info">
          <div class="music-title">{{ music.title }}</div>
          <div class="music-artist">{{ music.artist }}</div>
        </div>
        <div class="music-duration">{{ formatDuration(music.durationSeconds) }}</div>
        <div class="music-actions">
          <button @click.stop="toggleFavorite(music)" class="favorite-btn">
            {{ isFavorite(music.id) ? '★' : '☆' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useMusicStore } from '@/store/modules/music'
import { useFavoriteStore } from '@/store/modules/favorite'
import { Music } from '@/types/models/music'

const musicStore = useMusicStore()
const favoriteStore = useFavoriteStore()

// 响应式数据
const searchKeyword = ref('')

// 计算属性
const musicList = computed(() => musicStore.musicList)

// 方法
const selectMusic = (music: Music) => {
  musicStore.setCurrentMusic(music)
}

const handleSearch = () => {
  if (searchKeyword.value.trim() !== '') {
    musicStore.searchMusic(searchKeyword.value)
  } else {
    musicStore.fetchMusicList()
  }
}

const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}

const toggleFavorite = (music: Music) => {
  if (isFavorite(music.id)) {
    // 如果已收藏，则取消收藏
    console.log('Remove from favorites:', music.id)
    // 这里应该调用实际的API来移除收藏
  } else {
    // 如果未收藏，则添加到收藏
    console.log('Add to favorites:', music.id)
    // 这里应该调用实际的API来添加收藏
  }
}

const isFavorite = (musicId: number) => {
  return favoriteStore.favorites.some(fav => fav.musicId === musicId)
}

// 生命周期钩子
onMounted(() => {
  musicStore.fetchMusicList()
})
</script>

<style scoped>
.music-list {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-xl);
  padding: var(--spacing-lg);
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow-xl);
  height: 450px;
  overflow-y: auto;
  position: relative;
  overflow: hidden;
  transition: all var(--transition-smoother);
}

.music-list::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.05), transparent);
  transition: left var(--transition-smoother);
}

.music-list:hover::before {
  left: 100%;
}

.music-list:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-double-glow);
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-xl);
  position: relative;
}

.list-header h2 {
  margin: 0;
  color: var(--primary-color);
  font-size: 24px;
  font-weight: 700;
  text-shadow: 0 0 12px var(--primary-glow);
  transition: all var(--transition-normal);
}

.music-list:hover .list-header h2 {
  color: var(--primary-light);
  text-shadow: 0 0 18px var(--primary-glow);
}

.search-box {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  background: rgba(21, 21, 32, 0.9);
  border: 1px solid var(--border-color);
  border-radius: var(--radius-lg);
  padding: var(--spacing-sm) var(--spacing-md);
  transition: all var(--transition-smoother);
  position: relative;
  overflow: hidden;
}

.search-box::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transition: left var(--transition-smoother);
}

.search-box:focus-within {
  border-color: var(--primary-color);
  box-shadow: 0 0 20px var(--primary-glow);
}

.search-box:focus-within::before {
  left: 100%;
}

.search-box input {
  background: transparent;
  border: none;
  outline: none;
  color: var(--text-primary);
  font-size: 14px;
  position: relative;
  z-index: 1;
  width: 250px;
}

.search-box input::placeholder {
  color: var(--text-muted);
}

.list-container {
  max-height: 400px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  position: relative;
}

.music-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
  padding: var(--spacing-md);
  border-radius: var(--radius-lg);
  background: rgba(21, 21, 32, 0.8);
  cursor: pointer;
  transition: all var(--transition-smoother);
  border: 1px solid transparent;
  position: relative;
  overflow: hidden;
}

.music-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.08), transparent);
  transition: left var(--transition-smoother);
}

.music-item:hover::before {
  left: 100%;
}

.music-item:hover {
  background: rgba(21, 21, 32, 0.95);
  transform: translateX(6px);
  border-color: var(--primary-color);
  box-shadow: 0 6px 20px var(--primary-glow);
}

.music-item.active {
  background: rgba(21, 21, 32, 0.95);
  border-color: var(--primary-color);
  box-shadow: 0 6px 20px var(--primary-glow), inset 0 0 20px rgba(0, 255, 255, 0.05);
  animation: activeGlow 2s ease-in-out infinite;
}

.music-cover {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  background: var(--gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
  box-shadow: 0 4px 12px var(--primary-glow);
  transition: all var(--transition-smoother);
  position: relative;
  overflow: hidden;
}

.music-cover::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left var(--transition-smoother);
}

.music-item:hover .music-cover::before {
  left: 100%;
}

.music-item:hover .music-cover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px var(--primary-glow);
}

.music-info {
  flex: 1;
  min-width: 0;
  position: relative;
}

.music-title {
  font-weight: 600;
  font-size: 15px;
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: all var(--transition-normal);
}

.music-item:hover .music-title {
  color: var(--primary-light);
}

.music-artist {
  font-size: 13px;
  color: var(--text-secondary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: all var(--transition-normal);
}

.music-item:hover .music-artist {
  color: var(--text-primary);
}

.music-duration {
  font-size: 13px;
  color: var(--text-secondary);
  min-width: 50px;
  transition: all var(--transition-normal);
  margin-right: 16px;
}

.music-item:hover .music-duration {
  color: var(--text-primary);
}

.favorite-btn {
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
  transition: all var(--transition-smoother);
  color: var(--text-secondary);
  position: relative;
  z-index: 1;
  padding: 4px;
  border-radius: 4px;
}

.favorite-btn:hover {
  transform: scale(1.3);
  text-shadow: 0 0 12px rgba(255, 71, 87, 0.5);
}

.favorite-btn.active {
  color: #ff4757;
  text-shadow: 0 0 12px rgba(255, 71, 87, 0.8);
  animation: heartBeat 0.8s ease;
}

@keyframes heartBeat {
  0% { transform: scale(1); }
  25% { transform: scale(1.4); }
  50% { transform: scale(1); }
  75% { transform: scale(1.3); }
  100% { transform: scale(1); }
}

@keyframes activeGlow {
  0%, 100% { box-shadow: 0 6px 20px var(--primary-glow), inset 0 0 20px rgba(0, 255, 255, 0.05); }
  50% { box-shadow: 0 6px 28px var(--primary-glow), inset 0 0 28px rgba(0, 255, 255, 0.08); }
}

/* 自定义滚动条 */
.list-container::-webkit-scrollbar {
  width: 10px;
}

.list-container::-webkit-scrollbar-track {
  background: rgba(21, 21, 32, 0.8);
  border-radius: var(--radius-full);
}

.list-container::-webkit-scrollbar-thumb {
  background: var(--gradient-primary);
  border-radius: var(--radius-full);
  transition: all var(--transition-smoother);
  box-shadow: 0 0 12px var(--primary-glow);
}

.list-container::-webkit-scrollbar-thumb:hover {
  background: var(--gradient-secondary);
  box-shadow: 0 0 18px var(--secondary-glow);
  transform: scale(1.1);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-list {
    padding: var(--spacing-md);
    height: 400px;
  }
  
  .list-header {
    flex-direction: column;
    gap: var(--spacing-md);
    align-items: stretch;
  }
  
  .list-header h2 {
    font-size: 20px;
    text-align: center;
  }
  
  .search-box {
    width: 100%;
  }
  
  .search-box input {
    width: 100%;
  }
  
  .music-item {
    gap: var(--spacing-md);
    padding: var(--spacing-sm);
  }
  
  .music-cover {
    width: 48px;
    height: 48px;
    font-size: 18px;
  }
  
  .music-title {
    font-size: 14px;
  }
  
  .music-artist {
    font-size: 12px;
  }
  
  .music-duration {
    font-size: 12px;
    min-width: 45px;
    margin-right: 10px;
  }
}

@media (max-width: 480px) {
  .music-list {
    padding: var(--spacing-sm);
    height: 350px;
  }
  
  .music-item {
    gap: var(--spacing-sm);
    padding: var(--spacing-xs);
  }
  
  .music-cover {
    width: 44px;
    height: 44px;
    font-size: 16px;
  }
  
  .music-title {
    font-size: 13px;
  }
  
  .music-artist {
    font-size: 11px;
  }
  
  .music-duration {
    font-size: 11px;
    min-width: 40px;
  }
  
  .favorite-btn {
    font-size: 18px;
  }
}
</style>