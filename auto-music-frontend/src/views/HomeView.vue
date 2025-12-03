<script setup lang="ts">
import MusicList from '@/components/audio/MusicList.vue'
import MusicDetail from '@/components/audio/MusicDetail.vue'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'
</script>

<template>
  <div class="home">
    <header class="header">
      <h1>星韵音域 - 音乐智能驾驶舱</h1>
      <p>欢迎来到您的个人音乐宇宙</p>
    </header>
    
    <main class="main-content">
      <div class="content-wrapper">
        <MusicList />
        <MusicDetail />
      </div>
      <AudioPlayer />
    </main>
  </div>
</template>

<style scoped>
.home {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.header {
  text-align: center;
  margin-bottom: 30px;
}

.header h1 {
  color: #2c3e50;
  margin-bottom: 10px;
}

.header p {
  color: #7f8c8d;
  font-size: 18px;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

@media (max-width: 768px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }
}
</style>