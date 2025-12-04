<template>
  <div class="audio-player">
    <div class="player-info">
      <div class="music-cover">
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M12 3v18a9 9 0 0 0 0-18z"/>
          <path d="M16.5 12a4.5 4.5 0 1 1-9 0 4.5 4.5 0 0 1 9 0z"/>
        </svg>
      </div>
      <div class="music-details">
        <h3 class="music-title">{{ currentMusic.title || '未选择音乐' }}</h3>
        <p class="music-artist">{{ currentMusic.artist || '未知艺术家' }}</p>
      </div>
    </div>

    <div class="player-controls">
      <button 
        class="control-btn shuffle-btn" 
        @click="toggleShuffle"
        :class="{ active: isShuffle }"
      >
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M10.59 9.17L5.41 4 4 5.41l5.17 5.17 1.42-1.41zM14.5 4l2.04 2.04L4 18.59 5.41 20 17.96 7.46 20 9.5V4h-5.5zm.33 9.41l-1.41 1.41 3.13 3.13L14.5 20H20v-5.5l-2.04 2.04-3.13-3.13z"/>
        </svg>
      </button>

      <button class="control-btn prev-btn" @click="playPrevious">
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M6 6h2v12H6zm3.5 6l8.5 6V6z"/>
        </svg>
      </button>

      <button 
        class="control-btn play-btn" 
        @click="togglePlayPause"
      >
        <svg v-if="isPlaying" viewBox="0 0 24 24" fill="currentColor">
          <path d="M6 4h4v16H6V4zm8 0h4v16h-4V4z"/>
        </svg>
        <svg v-else viewBox="0 0 24 24" fill="currentColor">
          <path d="M8 5v14l11-7z"/>
        </svg>
      </button>

      <button class="control-btn next-btn" @click="playNext">
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M6 18l8.5-6L6 6v12zM16 6v12h2V6h-2z"/>
        </svg>
      </button>

      <button 
        class="control-btn repeat-btn" 
        @click="toggleRepeat"
        :class="{ active: repeatMode }"
      >
        <svg viewBox="0 0 24 24" fill="currentColor">
          <path d="M7 7h10v3l4-4-4-4v3H5v6h2V7zm10 10H7v-3l-4 4 4 4v-3h12v-6h-2v4z"/>
        </svg>
      </button>
    </div>

    <div class="progress-bar">
      <span class="time-display">{{ formatTime(currentTime) }}</span>
      <input 
        type="range" 
        class="seek-bar" 
        min="0" 
        :max="duration || 100" 
        :value="currentTime" 
        @input="seek"
      />
      <span class="time-display">{{ formatTime(duration) }}</span>
    </div>

    <div class="volume-control">
      <button 
        class="control-btn volume-btn" 
        @click="toggleMute"
      >
        <svg v-if="volume > 0.5" viewBox="0 0 24 24" fill="currentColor">
          <path d="M3 9v6h4l5 5V4L7 9H3zm13.5 3c0-1.77-1.02-3.29-2.5-4.03v8.05c1.48-.73 2.5-2.25 2.5-4.02zM14 3.23v2.06c2.89.86 5 3.54 5 6.71s-2.11 5.85-5 6.71v2.06c4.01-.91 7-4.49 7-8.77s-2.99-7.86-7-8.77z"/>
        </svg>
        <svg v-else-if="volume > 0" viewBox="0 0 24 24" fill="currentColor">
          <path d="M7 9v6h4l5 5V4l-5 5H7z"/>
        </svg>
        <svg v-else viewBox="0 0 24 24" fill="currentColor">
          <path d="M16.5 12c0-1.77-1.02-3.29-2.5-4.03v2.21l2.45 2.45c.03-.2.05-.41.05-.63zm2.5 0c0 .94-.2 1.82-.54 2.64l1.51 1.51C20.63 14.91 21 13.5 21 12c0-4.28-2.99-7.86-7-8.77v2.06c2.89.86 5 3.54 5 6.71zM4.27 3L3 4.27 7.73 9H3v6h4l5 5v-6.73l4.25 4.25c-.67.52-1.42.93-2.25 1.18v2.06c1.38-.31 2.63-.95 3.69-1.81L19.73 21 21 19.73l-9-9L4.27 3zM12 4L9.91 6.09 12 8.18V4z"/>
        </svg>
      </button>
      <input 
        type="range" 
        class="volume-slider" 
        min="0" 
        max="1" 
        step="0.01" 
        :value="volume" 
        @input="changeVolume"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue'
import { Howl } from 'howler'

interface Music {
  id: string
  title: string
  artist: string
  url: string
  duration?: number
}

// Props
const props = defineProps<{
  music?: Music
  playlist?: Music[]
}>()

// Emits
const emit = defineEmits<{
  (e: 'music-ended'): void
  (e: 'music-changed', music: Music): void
}>()

// State
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const volume = ref(0.7)
const isMuted = ref(false)
const isShuffle = ref(false)
const repeatMode = ref<'none' | 'one' | 'all'>('none')
const sound = ref<Howl | null>(null)
const currentIndex = ref(0)

// Computed
const currentMusic = computed(() => {
  if (props.music) return props.music
  if (props.playlist && props.playlist.length > 0) {
    return props.playlist[currentIndex.value]
  }
  return { id: '', title: '', artist: '', url: '' }
})

// Methods
const formatTime = (seconds: number): string => {
  if (!seconds || isNaN(seconds)) return '0:00'
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const loadMusic = (music: Music) => {
  if (sound.value) {
    sound.value.unload()
  }

  sound.value = new Howl({
    src: [music.url],
    html5: true,
    volume: isMuted.value ? 0 : volume.value,
    onload: () => {
      duration.value = sound.value?.duration() || 0
    },
    onplay: () => {
      isPlaying.value = true
      requestAnimationFrame(updateProgress)
    },
    onpause: () => {
      isPlaying.value = false
    },
    onend: () => {
      isPlaying.value = false
      handleMusicEnd()
    },
    onstop: () => {
      isPlaying.value = false
      currentTime.value = 0
    }
  })
}

const togglePlayPause = () => {
  if (!sound.value && currentMusic.value.url) {
    loadMusic(currentMusic.value)
  }
  
  if (sound.value) {
    if (isPlaying.value) {
      sound.value.pause()
    } else {
      sound.value.play()
    }
  }
}

const playPrevious = () => {
  if (!props.playlist || props.playlist.length === 0) return
  
  if (isShuffle.value) {
    currentIndex.value = Math.floor(Math.random() * props.playlist.length)
  } else {
    currentIndex.value = (currentIndex.value - 1 + props.playlist.length) % props.playlist.length
  }
  
  const prevMusic = props.playlist[currentIndex.value]
  loadMusic(prevMusic)
  sound.value?.play()
  emit('music-changed', prevMusic)
}

const playNext = () => {
  if (!props.playlist || props.playlist.length === 0) return
  
  if (isShuffle.value) {
    currentIndex.value = Math.floor(Math.random() * props.playlist.length)
  } else {
    currentIndex.value = (currentIndex.value + 1) % props.playlist.length
  }
  
  const nextMusic = props.playlist[currentIndex.value]
  loadMusic(nextMusic)
  sound.value?.play()
  emit('music-changed', nextMusic)
}

const handleMusicEnd = () => {
  if (repeatMode.value === 'one' && currentMusic.value.url) {
    sound.value?.play()
  } else if (repeatMode.value === 'all') {
    playNext()
  } else {
    emit('music-ended')
  }
}

const seek = (event: Event) => {
  const target = event.target as HTMLInputElement
  const newTime = parseFloat(target.value)
  currentTime.value = newTime
  sound.value?.seek(newTime)
}

const changeVolume = (event: Event) => {
  const target = event.target as HTMLInputElement
  const newVolume = parseFloat(target.value)
  volume.value = newVolume
  if (sound.value) {
    sound.value.volume(newVolume)
    isMuted.value = newVolume === 0
  }
}

const toggleMute = () => {
  if (!sound.value) return
  
  isMuted.value = !isMuted.value
  sound.value.volume(isMuted.value ? 0 : volume.value)
}

const toggleShuffle = () => {
  isShuffle.value = !isShuffle.value
}

const toggleRepeat = () => {
  const modes: Array<'none' | 'one' | 'all'> = ['none', 'one', 'all']
  const currentModeIndex = modes.indexOf(repeatMode.value)
  repeatMode.value = modes[(currentModeIndex + 1) % modes.length]
}

const updateProgress = () => {
  if (sound.value && isPlaying.value) {
    currentTime.value = sound.value.seek() || 0
    requestAnimationFrame(updateProgress)
  }
}

// Watchers
watch(() => props.music, (newMusic) => {
  if (newMusic && newMusic.url) {
    loadMusic(newMusic)
    if (isPlaying.value) {
      sound.value?.play()
    }
  }
}, { immediate: true })

watch(() => props.playlist, (newPlaylist) => {
  if (newPlaylist && newPlaylist.length > 0 && !props.music) {
    currentIndex.value = 0
    loadMusic(newPlaylist[0])
  }
}, { immediate: true })

// Lifecycle
onMounted(() => {
  if (currentMusic.value.url && !sound.value) {
    loadMusic(currentMusic.value)
  }
})

onUnmounted(() => {
  if (sound.value) {
    sound.value.unload()
  }
})
</script>

<style scoped>
.audio-player {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-color);
  padding: var(--spacing-xl);
  margin-top: var(--spacing-xl);
  box-shadow: var(--shadow-xl);
  display: grid;
  grid-template-columns: 1fr auto 1fr;
  gap: var(--spacing-xl);
  align-items: center;
  position: relative;
  overflow: hidden;
  transition: all var(--transition-normal);
}

.audio-player::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.1), transparent);
  transition: left 0.5s ease;
}

.audio-player:hover::before {
  left: 100%;
}

.audio-player:hover {
  border-color: var(--primary-color);
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.3);
}

.player-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.music-cover {
  width: 60px;
  height: 60px;
  border-radius: var(--radius-lg);
  background: var(--gradient-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  box-shadow: var(--shadow-lg);
  transition: all var(--transition-normal);
}

.music-cover:hover {
  transform: scale(1.05);
  box-shadow: 0 0 20px var(--primary-glow);
}

.music-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.music-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
  transition: color var(--transition-fast);
}

.music-title:hover {
  color: var(--primary-color);
}

.music-artist {
  font-size: 14px;
  color: var(--text-secondary);
  margin: 0;
  transition: color var(--transition-fast);
}

.music-artist:hover {
  color: var(--primary-color);
}

.player-controls {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.control-btn {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-size: 20px;
  cursor: pointer;
  padding: var(--spacing-sm);
  border-radius: var(--radius-md);
  transition: all var(--transition-fast);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
}

.control-btn:hover {
  background: var(--bg-tertiary);
  color: var(--text-primary);
  transform: translateY(-2px);
}

.control-btn.active {
  color: var(--primary-color);
}

.play-btn {
  width: 56px;
  height: 56px;
  font-size: 24px;
  background: var(--gradient-primary);
  color: white;
  border-radius: 50%;
  box-shadow: var(--shadow-glow);
  transition: all var(--transition-normal);
}

.play-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 0 25px var(--primary-glow);
}

.play-btn:active {
  transform: scale(0.95);
}

.progress-bar {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.seek-bar {
  flex: 1;
  height: 6px;
  border-radius: 3px;
  background: var(--bg-tertiary);
  outline: none;
  -webkit-appearance: none;
}

.seek-bar::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--primary-color);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.seek-bar::-webkit-slider-thumb:hover {
  transform: scale(1.3);
  box-shadow: 0 0 0 6px rgba(0, 212, 255, 0.2);
}

.seek-bar::-moz-range-thumb {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: var(--primary-color);
  cursor: pointer;
  border: none;
  transition: all var(--transition-fast);
}

.seek-bar::-moz-range-thumb:hover {
  transform: scale(1.3);
  box-shadow: 0 0 0 6px rgba(0, 212, 255, 0.2);
}

.time-display {
  font-size: 12px;
  color: var(--text-secondary);
  min-width: 80px;
  text-align: center;
  transition: color var(--transition-fast);
}

.time-display:hover {
  color: var(--primary-color);
}

.volume-control {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.volume-slider {
  width: 100px;
  height: 6px;
  border-radius: 3px;
  background: var(--bg-tertiary);
  outline: none;
  -webkit-appearance: none;
  transition: all var(--transition-fast);
}

.volume-slider:hover {
  background: var(--bg-secondary);
}

.volume-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: var(--primary-color);
  cursor: pointer;
  transition: all var(--transition-fast);
}

.volume-slider::-webkit-slider-thumb:hover {
  transform: scale(1.2);
  box-shadow: 0 0 0 4px rgba(0, 212, 255, 0.2);
}

.volume-slider::-moz-range-thumb {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: var(--primary-color);
  cursor: pointer;
  border: none;
  transition: all var(--transition-fast);
}

.volume-slider::-moz-range-thumb:hover {
  transform: scale(1.2);
  box-shadow: 0 0 0 4px rgba(0, 212, 255, 0.2);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .audio-player {
    padding: 15px;
    margin-top: 15px;
    grid-template-columns: 1fr;
    gap: var(--spacing-md);
  }
  
  .player-controls {
    gap: 15px;
    margin-bottom: 15px;
  }
  
  .play-btn {
    width: 44px;
    height: 44px;
    font-size: 18px;
  }
  
  .volume-control input {
    width: 80px;
  }
  
  .progress-bar {
    gap: 12px;
  }
  
  .time-display {
    min-width: 60px;
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  .audio-player {
    padding: 12px;
    margin-top: 12px;
    gap: var(--spacing-sm);
  }
  
  .player-controls {
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .control-btn {
    width: 36px;
    height: 36px;
    font-size: 18px;
  }
  
  .play-btn {
    width: 40px;
    height: 40px;
    font-size: 16px;
  }
  
  .volume-control input {
    width: 60px;
  }
  
  .progress-bar {
    gap: 8px;
  }
  
  .time-display {
    min-width: 50px;
    font-size: 10px;
  }
}
</style>