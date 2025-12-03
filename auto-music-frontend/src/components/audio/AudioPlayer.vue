<template>
  <div class="audio-player">
    <div class="player-controls">
      <button @click="togglePlay" class="play-btn">
        {{ isPlaying ? '⏸' : '▶' }}
      </button>
      <div class="track-info">
        <div class="track-title">{{ currentMusic?.title || '未选择音乐' }}</div>
        <div class="track-artist">{{ currentMusic?.artist || '' }}</div>
      </div>
      <div class="volume-control">
        <input 
          type="range" 
          min="0" 
          max="1" 
          step="0.01" 
          v-model="volume" 
          @input="setVolume"
        />
      </div>
    </div>
    <div class="progress-bar">
      <input 
        type="range" 
        min="0" 
        :max="duration" 
        :value="currentTime" 
        @input="seek" 
        class="seek-bar"
      />
      <div class="time-display">
        {{ formatTime(currentTime) }} / {{ formatTime(duration) }}
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useMusicStore } from '@/store/modules/music'

const musicStore = useMusicStore()
const audio = new Audio()

// 响应式数据
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(0.7)

// 计算属性
const currentMusic = computed(() => musicStore.currentMusic)

// 方法
const togglePlay = () => {
  if (isPlaying.value) {
    audio.pause()
  } else {
    audio.play()
  }
  isPlaying.value = !isPlaying.value
}

const setVolume = () => {
  audio.volume = volume.value
}

const seek = (event: Event) => {
  const target = event.target as HTMLInputElement
  audio.currentTime = parseFloat(target.value)
}

const formatTime = (seconds: number) => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs < 10 ? '0' : ''}${secs}`
}

// 监听当前音乐变化
watch(currentMusic, (newMusic) => {
  if (newMusic) {
    audio.src = newMusic.filePath
    audio.load()
    duration.value = newMusic.durationSeconds
  }
})

// 音频事件监听
audio.addEventListener('timeupdate', () => {
  currentTime.value = audio.currentTime
})

audio.addEventListener('loadedmetadata', () => {
  duration.value = audio.duration
})

audio.addEventListener('ended', () => {
  isPlaying.value = false
})
</script>

<style scoped>
.audio-player {
  background: #2c3e50;
  border-radius: 8px;
  padding: 16px;
  color: white;
}

.player-controls {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
}

.play-btn {
  background: #3498db;
  border: none;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  font-size: 16px;
  cursor: pointer;
}

.track-info {
  flex: 1;
}

.track-title {
  font-weight: bold;
  font-size: 16px;
}

.track-artist {
  font-size: 14px;
  opacity: 0.8;
}

.volume-control input {
  width: 100px;
}

.progress-bar {
  display: flex;
  align-items: center;
  gap: 16px;
}

.seek-bar {
  flex: 1;
  height: 6px;
}

.time-display {
  font-size: 12px;
  opacity: 0.8;
}
</style>