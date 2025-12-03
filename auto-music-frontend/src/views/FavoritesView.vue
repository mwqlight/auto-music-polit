<template>
  <MainLayout>
    <div class="favorites-view">
      <div class="favorites-header">
        <h1>我的收藏</h1>
      </div>
      
      <div v-if="favorites.length > 0" class="favorites-list">
        <div 
          v-for="favorite in favorites" 
          :key="favorite.id"
          class="favorite-item"
          @click="selectMusic(favorite.music)"
        >
          <div class="music-info">
            <h3>{{ favorite.music.title }}</h3>
            <p>{{ favorite.music.artist }}</p>
          </div>
          <div class="music-duration">
            {{ formatDuration(favorite.music.durationSeconds) }}
          </div>
          <button @click.stop="removeFromFavorites(favorite)" class="remove-btn">取消收藏</button>
        </div>
      </div>
      
      <div v-else class="no-favorites">
        <p>您还没有收藏任何音乐</p>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useFavoriteStore } from '@/store/modules/favorite'
import { useMusicStore } from '@/store/modules/music'
import MainLayout from '@/components/layout/MainLayout.vue'

const favoriteStore = useFavoriteStore()
const musicStore = useMusicStore()

// 计算属性
const favorites = computed(() => favoriteStore.favorites)

// 方法
const selectMusic = (music: any) => {
  musicStore.setCurrentMusic(music)
}

const removeFromFavorites = (favorite: any) => {
  favoriteStore.removeFavorite(favorite.music.id)
}

const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}
</script>

<style scoped>
.favorites-view {
  max-width: 1200px;
  margin: 0 auto;
}

.favorites-header h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.favorites-list {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
}

.favorite-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.favorite-item:hover {
  background: #f5f5f5;
}

.music-info {
  flex: 1;
}

.music-info h3 {
  margin: 0 0 5px 0;
  color: #2c3e50;
}

.music-info p {
  margin: 0;
  color: #7f8c8d;
}

.music-duration {
  color: #7f8c8d;
  margin-right: 20px;
}

.remove-btn {
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
}

.no-favorites {
  background: white;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.no-favorites p {
  color: #7f8c8d;
  font-size: 18px;
}
</style>