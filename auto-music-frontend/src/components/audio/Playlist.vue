<template>
  <div class="playlist">
    <div class="playlist-header">
      <h2>{{ playlist?.name || '播放列表' }}</h2>
      <button @click="createNewPlaylist" class="create-btn">新建播放列表</button>
    </div>
    <div class="playlist-items">
      <div 
        v-for="item in playlist?.musics" 
        :key="item.id" 
        class="playlist-item"
        @click="selectMusic(item)"
      >
        <div class="item-info">
          <div class="item-title">{{ item.title }}</div>
          <div class="item-artist">{{ item.artist }}</div>
        </div>
        <div class="item-duration">{{ formatDuration(item.durationSeconds) }}</div>
        <div class="item-actions">
          <button @click.stop="removeFromPlaylist(item)" class="remove-btn">移除</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { usePlaylistStore } from '@/store/modules/playlist'
import { useMusicStore } from '@/store/modules/music'
import { Music } from '@/types/models/music'

const playlistStore = usePlaylistStore()
const musicStore = useMusicStore()

// 计算属性
const playlist = computed(() => playlistStore.currentPlaylist)

// 方法
const selectMusic = (music: Music) => {
  musicStore.setCurrentMusic(music)
}

const formatDuration = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}

const createNewPlaylist = () => {
  // 实现创建新播放列表的逻辑
  console.log('Create new playlist')
}

const removeFromPlaylist = (music: Music) => {
  // 实现从播放列表移除音乐的逻辑
  console.log('Remove music from playlist:', music)
}
</script>

<style scoped>
.playlist {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.playlist-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #eee;
}

.playlist-header h2 {
  margin: 0;
  color: #2c3e50;
}

.create-btn {
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 12px;
  cursor: pointer;
}

.playlist-items {
  max-height: 400px;
  overflow-y: auto;
}

.playlist-item {
  display: flex;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.2s;
}

.playlist-item:hover {
  background-color: #f5f5f5;
}

.item-info {
  flex: 1;
}

.item-title {
  font-weight: bold;
  color: #2c3e50;
}

.item-artist {
  font-size: 14px;
  color: #7f8c8d;
}

.item-duration {
  color: #7f8c8d;
  margin-right: 16px;
}

.remove-btn {
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 4px 8px;
  cursor: pointer;
}
</style>