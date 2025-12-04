<template>
  <MainLayout>
    <div class="music-library-view">
      <div class="library-header">
        <h1>音乐库</h1>
      </div>
      
      <MusicList />
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import MainLayout from '@/components/layout/MainLayout.vue'
import MusicList from '@/components/audio/MusicList.vue'
</script>

<style scoped>
.music-library-view {
  padding: 20px;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 50%, #16213e 100%);
  min-height: 100vh;
}

.library-header h1 {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: var(--text-primary);
  text-align: center;
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
}

.music-list-container {
  max-width: 1200px;
  margin: 0 auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .music-library-view {
    padding: 10px;
  }
  
  .library-header h1 {
    font-size: 2rem;
  }
}
</style>