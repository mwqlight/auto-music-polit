<template>
  <div class="playlists-view">
    <div class="playlists-header">
      <h1>播放列表</h1>
    </div>
    
    <div class="playlists-container">
      <div class="playlist-list">
        <div 
          v-for="playlist in playlists" 
          :key="playlist.id"
          class="playlist-item"
          @click="selectPlaylist(playlist)"
        >
          <h3>{{ playlist.name }}</h3>
          <p>{{ playlist.musics.length }} 首歌曲</p>
        </div>
      </div>
      
      <div class="playlist-detail">
        <Playlist v-if="currentPlaylist" /> 
        <div v-else class="placeholder">
          请选择一个播放列表
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { usePlaylistStore } from '@/store/modules/playlist'
import Playlist from '@/components/audio/Playlist.vue'

const playlistStore = usePlaylistStore()

// 计算属性
const playlists = computed(() => playlistStore.playlists)
const currentPlaylist = computed(() => playlistStore.currentPlaylist)

// 方法
const selectPlaylist = (playlist: any) => {
  playlistStore.setCurrentPlaylist(playlist)
}
</script>

<style scoped>
.playlists-view {
  max-width: 1200px;
  margin: 0 auto;
}

.playlists-header h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.playlists-container {
  display: flex;
  gap: 20px;
}

.playlist-list {
  flex: 1;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.playlist-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}

.playlist-item:last-child {
  border-bottom: none;
}

.playlist-item:hover {
  background: #f5f5f5;
}

.playlist-detail {
  flex: 2;
}

.placeholder {
  background: white;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  color: #7f8c8d;
}
</style>