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
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.list-header h2 {
  margin: 0;
  color: #2c3e50;
}

.search-box input {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  width: 200px;
}

.list-container {
  max-height: 400px;
  overflow-y: auto;
}

.music-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.2s;
}

.music-item:hover {
  background-color: #f5f5f5;
}

.music-info {
  flex: 1;
}

.music-title {
  font-weight: bold;
  color: #2c3e50;
}

.music-artist {
  font-size: 14px;
  color: #7f8c8d;
}

.music-duration {
  color: #7f8c8d;
  margin-right: 16px;
}

.favorite-btn {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #f39c12;
}
</style>