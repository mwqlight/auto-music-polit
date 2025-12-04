<template>
  <div id="app">
    <MainLayout />
    <AudioPlayer />
  </div>
</template>

<script setup lang="ts">
import MainLayout from '@/components/layout/MainLayout.vue'
import AudioPlayer from '@/components/audio/AudioPlayer.vue'
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700;900&family=Rajdhani:wght@300;400;500;600;700&display=swap');

#app {
  font-family: 'Rajdhani', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #00ffff;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: linear-gradient(135deg, #0a0e27 0%, #1a1a2e 50%, #16213e 100%);
  overflow-x: hidden;
}

* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  margin: 0;
  padding: 0;
  background: linear-gradient(135deg, #0a0e27 0%, #1a1a2e 50%, #16213e 100%);
}

/* 高科技风格的全局样式 */
.high-tech-button {
  background: linear-gradient(45deg, #00ffff, #0080ff);
  border: none;
  border-radius: 50px;
  padding: 12px 30px;
  font-family: 'Orbitron', sans-serif;
  font-size: 16px;
  font-weight: 700;
  color: #0a0e27;
  text-transform: uppercase;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
}

.high-tech-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.high-tech-button:hover::before {
  left: 100%;
}

.high-tech-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.5);
}

.high-tech-button:active {
  transform: translateY(-1px);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.high-tech-card {
  background: rgba(26, 26, 46, 0.8);
  border: 1px solid rgba(0, 255, 255, 0.2);
  border-radius: 15px;
  padding: 25px;
  backdrop-filter: blur(10px);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.high-tech-card:hover {
  border-color: rgba(0, 255, 255, 0.5);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.2);
  transform: translateY(-5px);
}

.high-tech-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 36px;
  font-weight: 900;
  color: #00ffff;
  text-align: center;
  margin-bottom: 10px;
  text-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
}

.high-tech-subtitle {
  font-family: 'Rajdhani', sans-serif;
  font-size: 18px;
  color: #0080ff;
  text-align: center;
  margin-bottom: 30px;
  letter-spacing: 2px;
}

/* 粒子效果背景 */
.particles-bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  overflow: hidden;
}

.particle {
  position: absolute;
  width: 2px;
  height: 2px;
  background: rgba(0, 255, 255, 0.5);
  border-radius: 50%;
  animation: float 10s infinite linear;
}

@keyframes float {
  0% {
    transform: translateY(100vh) translateX(0) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100px) translateX(100px) rotate(360deg);
    opacity: 0;
  }
}
</style>