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
  position: relative;
}

/* 头部样式 */
.header {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  color: var(--text-primary);
  padding: 0 var(--spacing-lg);
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: var(--shadow-xl);
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 1000;
  height: 70px;
  transition: all var(--transition-smoother);
}

.header:hover {
  border-bottom-color: var(--primary-color);
  box-shadow: var(--shadow-double-glow);
}

/* Logo样式 */
.logo h1 {
  margin: 0;
  font-size: 28px;
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  text-shadow: 0 0 20px var(--primary-glow);
  animation: glow 3s ease-in-out infinite;
  font-weight: 700;
  letter-spacing: 1px;
}

/* 导航样式 */
.navigation {
  display: flex;
  gap: var(--spacing-md);
  align-items: center;
}

.navigation a {
  color: var(--text-secondary);
  text-decoration: none;
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--radius-lg);
  transition: all var(--transition-smoother);
  font-weight: 600;
  font-size: 14px;
  position: relative;
  overflow: hidden;
}

.navigation a::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transition: left var(--transition-smoother);
}

.navigation a:hover::before {
  left: 100%;
}

.navigation a.router-link-exact-active,
.navigation a:hover {
  color: var(--primary-color);
  background: var(--bg-tertiary);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
  border: 1px solid var(--primary-color);
}

.navigation a.router-link-exact-active {
  background: rgba(0, 255, 255, 0.1);
  border-color: var(--primary-color);
  box-shadow: 0 0 10px var(--primary-glow);
}

/* 用户操作区域 */
.user-actions {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.auth-btn {
  padding: var(--spacing-sm) var(--spacing-lg);
  background: var(--gradient-primary);
  color: var(--text-primary);
  border: none;
  border-radius: var(--radius-lg);
  cursor: pointer;
  font-weight: 600;
  transition: all var(--transition-smoother);
  font-size: 14px;
  position: relative;
  overflow: hidden;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.auth-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left var(--transition-smoother);
}

.auth-btn:hover::before {
  left: 100%;
}

.auth-btn:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-double-glow);
  animation: glow 2s ease-in-out infinite;
}

/* 用户资料区域 */
.user-profile {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-sm) var(--spacing-md);
  background: var(--bg-tertiary);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-color);
  transition: all var(--transition-smoother);
  position: relative;
  overflow: hidden;
}

.user-profile::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 255, 255, 0.1), transparent);
  transition: left var(--transition-smoother);
}

.user-profile:hover::before {
  left: 100%;
}

.user-profile:hover {
  border-color: var(--primary-color);
  box-shadow: var(--shadow-md);
  transform: translateY(-2px);
}

.user-profile span {
  font-weight: 600;
  color: var(--text-primary);
}

/* 主要内容区域 */
.main-content {
  flex: 1;
  padding: var(--spacing-xl);
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

/* 页脚样式 */
.footer {
  background: rgba(21, 21, 32, 0.95);
  backdrop-filter: blur(10px);
  color: var(--text-secondary);
  padding: var(--spacing-lg);
  text-align: center;
  border-top: 1px solid var(--border-color);
  transition: all var(--transition-smoother);
}

.footer:hover {
  border-top-color: var(--primary-color);
  box-shadow: 0 -4px 16px rgba(0, 255, 255, 0.2);
}

.footer-content p {
  margin: 0;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .navigation {
    gap: var(--spacing-sm);
  }
  
  .navigation a {
    padding: var(--spacing-xs) var(--spacing-sm);
    font-size: 13px;
  }
}

@media (max-width: 768px) {
  .header {
    padding: 0 var(--spacing-md);
    height: 60px;
  }
  
  .logo h1 {
    font-size: 22px;
  }
  
  .navigation {
    gap: var(--spacing-xs);
  }
  
  .navigation a {
    padding: var(--spacing-xs) var(--spacing-sm);
    font-size: 12px;
  }
  
  .main-content {
    padding: var(--spacing-lg);
  }
  
  .user-actions {
    gap: var(--spacing-sm);
  }
  
  .auth-btn {
    padding: var(--spacing-xs) var(--spacing-md);
    font-size: 12px;
  }
  
  .user-profile {
    padding: var(--spacing-xs) var(--spacing-sm);
  }
  
  .user-profile span {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 0 var(--spacing-sm);
  }
  
  .logo h1 {
    font-size: 18px;
  }
  
  .navigation {
    display: none;
  }
  
  .main-content {
    padding: var(--spacing-md);
  }
}
</style>