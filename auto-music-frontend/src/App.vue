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
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  margin: 0;
  padding: 0;
  background-color: #ecf0f1;
}
</style>