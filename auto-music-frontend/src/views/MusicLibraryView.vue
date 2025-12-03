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
  max-width: 1200px;
  margin: 0 auto;
}

.library-header h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}
</style>