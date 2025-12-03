<template>
  <div class="main-layout">
    <header class="header">
      <div class="logo">
        <h1>星韵音域</h1>
      </div>
      <nav class="navigation">
        <router-link to="/">首页</router-link>
        <router-link to="/music">音乐库</router-link>
        <router-link to="/playlists">播放列表</router-link>
        <router-link to="/favorites">我的收藏</router-link>
        <router-link to="/ai-music">AI音乐</router-link>
        <router-link to="/profile">个人中心</router-link>
      </nav>
      <div class="user-actions">
        <button v-if="!isAuthenticated" @click="login" class="auth-btn">登录</button>
        <div v-else class="user-profile">
          <span>{{ currentUser?.username }}</span>
          <button @click="logout" class="auth-btn">退出</button>
        </div>
      </div>
    </header>
    
    <main class="main-content">
      <slot></slot>
    </main>
    
    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2023 星韵音域. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useUserStore } from '@/store/modules/user'

const userStore = useUserStore()

// 计算属性
const isAuthenticated = computed(() => userStore.isAuthenticated)
const currentUser = computed(() => userStore.currentUser)

// 方法
const login = () => {
  // 实现登录逻辑
  console.log('Login')
}

const logout = () => {
  userStore.logout()
}
</script>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.header {
  background: #2c3e50;
  color: white;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.logo h1 {
  margin: 0;
  font-size: 24px;
}

.navigation {
  display: flex;
  gap: 20px;
}

.navigation a {
  color: white;
  text-decoration: none;
  padding: 10px 15px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.navigation a.router-link-exact-active,
.navigation a:hover {
  background-color: #34495e;
}

.user-actions {
  display: flex;
  align-items: center;
}

.auth-btn {
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  margin-left: 10px;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 10px;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #ecf0f1;
}

.footer {
  background: #34495e;
  color: white;
  padding: 20px;
  text-align: center;
}
</style>