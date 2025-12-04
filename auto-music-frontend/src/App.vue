<template>
  <div id="app">
    <router-view />
    <AudioPlayer />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'
</script>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>