<template>
  <div class="main-layout">
    <header class="header">
      <div class="logo">
        <h1>星韵音域</h1>
      </div>
      <div class="user-actions">
        <button v-if="!isAuthenticated" @click="login" class="auth-btn">登录</button>
        <div v-else class="user-profile">
          <span>{{ currentUser?.username }}</span>
          <button @click="logout" class="auth-btn">退出</button>
        </div>
      </div>
    </header>
    
    <div class="content-wrapper">
      <aside class="sidebar">
        <nav class="sidebar-navigation">
          <router-link to="/" :class="{ 'active': $route.path === '/' }">
            <Home class="icon" size="20" />
            <span>首页</span>
          </router-link>
          <router-link to="/music" :class="{ 'active': $route.path.startsWith('/music') }">
            <Music class="icon" size="20" />
            <span>音乐库</span>
          </router-link>
          <router-link to="/playlists" :class="{ 'active': $route.path.startsWith('/playlists') }">
            <ListMusic class="icon" size="20" />
            <span>播放列表</span>
          </router-link>
          <router-link to="/favorites" :class="{ 'active': $route.path.startsWith('/favorites') }">
            <Heart class="icon" size="20" />
            <span>我的收藏</span>
          </router-link>
          <router-link to="/ai-music" :class="{ 'active': $route.path.startsWith('/ai-music') }">
            <Zap class="icon" size="20" />
            <span>AI音乐</span>
          </router-link>
          <router-link to="/crawler" :class="{ 'active': $route.path.startsWith('/crawler') }">
            <Search class="icon" size="20" />
            <span>音乐爬取</span>
          </router-link>
          <router-link to="/profile" :class="{ 'active': $route.path.startsWith('/profile') }">
            <User class="icon" size="20" />
            <span>个人中心</span>
          </router-link>
        </nav>
      </aside>
      
      <main class="main-content">
        <slot></slot>
      </main>
    </div>
    
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
import { Home, Music, ListMusic, Heart, Zap, Search, User } from 'lucide-vue-next'

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
  background: linear-gradient(135deg, #0a0e27 0%, #1a1a2e 50%, #16213e 100%);
}

/* 现代化头部样式 */
.header {
  background: rgba(26, 26, 46, 0.9);
  border-bottom: 1px solid rgba(0, 255, 255, 0.2);
  color: #00ffff;
  padding: 15px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  backdrop-filter: blur(15px);
  box-shadow: 0 4px 20px rgba(0, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.header:hover {
  box-shadow: 0 6px 30px rgba(0, 255, 255, 0.2);
  border-bottom-color: rgba(0, 255, 255, 0.4);
}

.logo h1 {
  margin: 0;
  font-family: 'Orbitron', sans-serif;
  font-size: 28px;
  font-weight: 700;
  color: #00ffff;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
  letter-spacing: 2px;
}

/* 现代化用户操作区域 */
.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.auth-btn {
  background: linear-gradient(45deg, #00ffff, #0080ff);
  border: none;
  border-radius: 25px;
  padding: 10px 24px;
  font-family: 'Orbitron', sans-serif;
  font-size: 14px;
  font-weight: 600;
  color: #0a0e27;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.2);
  position: relative;
  overflow: hidden;
}

.auth-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.auth-btn:hover::before {
  left: 100%;
}

.auth-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 25px rgba(0, 255, 255, 0.4);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 8px 16px;
  background: rgba(0, 255, 255, 0.1);
  border-radius: 25px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.user-profile:hover {
  background: rgba(0, 255, 255, 0.15);
  border-color: rgba(0, 255, 255, 0.4);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.2);
}

.user-profile span {
  color: #00ffff;
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  font-weight: 500;
}

/* 现代化内容容器 */
.content-wrapper {
  display: flex;
  flex: 1;
  padding: 20px;
  gap: 20px;
  position: relative;
}

.content-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 80%, rgba(0, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(0, 128, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

/* 现代化侧边栏样式 */
.sidebar {
  width: 260px;
  background: rgba(26, 26, 46, 0.85);
  border: 1px solid rgba(0, 255, 255, 0.2);
  border-radius: 15px;
  padding: 25px 0;
  backdrop-filter: blur(15px);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.sidebar:hover {
  border-color: rgba(0, 255, 255, 0.4);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.2);
  transform: translateY(-5px);
}

/* 现代化侧边栏导航 */
.sidebar-navigation {
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 0 15px;
}

.sidebar-navigation a {
  color: rgba(0, 255, 255, 0.7);
  text-decoration: none;
  padding: 15px 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 15px;
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  font-weight: 500;
  position: relative;
  overflow: hidden;
}

.sidebar-navigation a::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transition: left 0.5s;
}

.sidebar-navigation a:hover::before {
  left: 100%;
}

.sidebar-navigation a:hover {
  background: rgba(0, 255, 255, 0.1);
  color: #00ffff;
  transform: translateX(5px);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.2);
}

.sidebar-navigation a.active {
  background: linear-gradient(45deg, rgba(0, 255, 255, 0.2), rgba(0, 128, 255, 0.2));
  color: #00ffff;
  border: 1px solid rgba(0, 255, 255, 0.3);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.3);
}

.sidebar-navigation .icon {
  font-size: 20px;
  width: 24px;
  text-align: center;
  filter: drop-shadow(0 0 5px rgba(0, 255, 255, 0.5));
}

/* 现代化主内容区域 */
.main-content {
  flex: 1;
  background: rgba(26, 26, 46, 0.85);
  border: 1px solid rgba(0, 255, 255, 0.2);
  border-radius: 15px;
  padding: 30px;
  backdrop-filter: blur(15px);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.main-content:hover {
  border-color: rgba(0, 255, 255, 0.4);
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.2);
  transform: translateY(-5px);
}

/* 现代化页脚样式 */
.footer {
  background: rgba(26, 26, 46, 0.95);
  border-top: 1px solid rgba(0, 255, 255, 0.2);
  color: rgba(0, 255, 255, 0.7);
  text-align: center;
  padding: 25px;
  backdrop-filter: blur(15px);
  box-shadow: 0 -4px 20px rgba(0, 255, 255, 0.1);
  transition: all 0.3s ease;
}

.footer:hover {
  border-top-color: rgba(0, 255, 255, 0.4);
  box-shadow: 0 -6px 30px rgba(0, 255, 255, 0.2);
}

.footer-content p {
  margin: 0;
  font-family: 'Rajdhani', sans-serif;
  font-size: 14px;
  letter-spacing: 1px;
}
</style>