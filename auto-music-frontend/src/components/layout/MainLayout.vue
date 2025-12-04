<template>
  <div class="main-layout">
    <!-- 侧边栏 -->
    <aside class="sidebar" :class="{ 'sidebar-collapsed': isSidebarCollapsed }">
      <div class="sidebar-header">
        <div class="logo">
          <i class="el-icon-music"></i>
          <h1 v-if="!isSidebarCollapsed">星韵音域</h1>
        </div>
        <el-button
          type="text"
          class="collapse-button"
          @click="toggleSidebar"
          :icon="isSidebarCollapsed ? 'el-icon-s-unfold' : 'el-icon-s-fold'"
        />
      </div>
      
      <nav class="sidebar-nav">
        <div
          v-for="menuItem in menuItems"
          :key="menuItem.path"
          class="nav-item"
          :class="{ 'active': $route.path === menuItem.path }"
          @click="navigateTo(menuItem.path)"
        >
          <i :class="menuItem.icon"></i>
          <span v-if="!isSidebarCollapsed">{{ menuItem.label }}</span>
          <div v-if="isSidebarCollapsed" class="tooltip">{{ menuItem.label }}</div>
        </div>
      </nav>
      
      <div class="sidebar-footer">
        <div class="user-info" v-if="!isSidebarCollapsed">
          <el-avatar icon="el-icon-user" />
          <span>音乐管理员</span>
        </div>
      </div>
    </aside>
    
    <!-- 主内容区域 -->
    <main class="main-content" :class="{ 'expanded': isSidebarCollapsed }">
      <header class="content-header">
        <div class="header-actions">
          <el-button
            type="text"
            icon="el-icon-bell"
            class="notification-button"
          />
          <el-button
            type="text"
            icon="el-icon-setting"
            class="settings-button"
          />
        </div>
      </header>
      
      <div class="content-body">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isSidebarCollapsed = ref(false)

const menuItems = [
  { path: '/', label: '首页', icon: 'el-icon-s-home' },
  { path: '/music-library', label: '音乐库', icon: 'el-icon-folder-opened' },
  { path: '/ai-music', label: 'AI音乐生成', icon: 'el-icon-s-tools' },
  { path: '/playlists', label: '播放列表', icon: 'el-icon-menu' },
  { path: '/favorites', label: '我的收藏', icon: 'el-icon-star-on' },
  { path: '/music-crawler', label: '音乐源爬取', icon: 'el-icon-download' },
  { path: '/recommendations', label: '智能推荐', icon: 'el-icon-search' },
  { path: '/audio-lab', label: '音频实验室', icon: 'el-icon-flask' }
]

const toggleSidebar = () => {
  isSidebarCollapsed.value = !isSidebarCollapsed.value
}

const navigateTo = (path: string) => {
  router.push(path)
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700;900&family=Rajdhani:wght@300;400;500;600;700&display=swap');
@import url('https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css');

* {
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.main-layout {
  display: flex;
  height: 100vh;
  background: linear-gradient(135deg, #0a0e27 0%, #1a1a2e 50%, #16213e 100%);
  overflow: hidden;
  position: relative;
}

/* 侧边栏样式 */
.sidebar {
  width: 260px;
  background: rgba(26, 26, 46, 0.95);
  border-right: 1px solid rgba(0, 255, 255, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  z-index: 1000;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.3);
}

.sidebar-collapsed {
  width: 70px;
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid rgba(0, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(0, 255, 255, 0.05);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #00ffff;
  flex: 1;
}

.logo i {
  font-size: 28px;
  color: #00ffff;
  min-width: 28px;
  text-align: center;
}

.logo h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 700;
  font-family: 'Orbitron', sans-serif;
  color: #00ffff;
  text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
  white-space: nowrap;
}

.collapse-button {
  color: #00ffff !important;
  font-size: 18px;
  padding: 8px !important;
  min-width: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.collapse-button:hover {
  background: rgba(0, 255, 255, 0.1) !important;
}

/* 导航菜单 */
.sidebar-nav {
  flex: 1;
  padding: 20px 10px;
  overflow-y: auto;
  overflow-x: hidden;
}

.sidebar-nav::-webkit-scrollbar {
  width: 4px;
}

.sidebar-nav::-webkit-scrollbar-track {
  background: rgba(0, 255, 255, 0.05);
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background: rgba(0, 255, 255, 0.3);
  border-radius: 2px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 15px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  color: #00ffff;
  position: relative;
  white-space: nowrap;
}

.nav-item:hover {
  background: rgba(0, 255, 255, 0.1);
  transform: translateX(5px);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.2);
}

.nav-item.active {
  background: linear-gradient(45deg, rgba(0, 255, 255, 0.2), rgba(0, 128, 255, 0.2));
  border-left: 3px solid #00ffff;
}

.nav-item i {
  font-size: 20px;
  color: #00ffff;
  min-width: 20px;
  text-align: center;
}

.nav-item span {
  font-size: 14px;
  font-weight: 500;
  white-space: nowrap;
}

/* 工具提示 */
.tooltip {
  position: absolute;
  left: 100%;
  margin-left: 10px;
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.9);
  color: #00ffff;
  border-radius: 6px;
  font-size: 12px;
  white-space: nowrap;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 255, 255, 0.3);
  z-index: 1001;
}

.nav-item:hover .tooltip {
  opacity: 1;
  visibility: visible;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid rgba(0, 255, 255, 0.1);
  background: rgba(0, 255, 255, 0.05);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #00ffff;
}

.user-info .el-avatar {
  background: rgba(0, 255, 255, 0.2);
  border: 1px solid rgba(0, 255, 255, 0.3);
}

.user-info span {
  font-size: 14px;
  font-weight: 500;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  margin-left: 260px;
  transition: margin-left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden;
}

.main-content.expanded {
  margin-left: 70px;
}

/* 内容头部 */
.content-header {
  padding: 20px 30px;
  background: rgba(26, 26, 46, 0.8);
  border-bottom: 1px solid rgba(0, 255, 255, 0.1);
  display: flex;
  justify-content: flex-end;
  align-items: center;
  backdrop-filter: blur(10px);
}

.header-actions {
  display: flex;
  gap: 15px;
}

.notification-button,
.settings-button {
  color: #00ffff !important;
  font-size: 18px;
  padding: 10px !important;
  min-width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.notification-button:hover,
.settings-button:hover {
  background: rgba(0, 255, 255, 0.1) !important;
}

/* 内容主体 */
.content-body {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.content-body::-webkit-scrollbar {
  width: 8px;
}

.content-body::-webkit-scrollbar-track {
  background: rgba(0, 255, 255, 0.05);
}

.content-body::-webkit-scrollbar-thumb {
  background: rgba(0, 255, 255, 0.3);
  border-radius: 4px;
}

.content-body::-webkit-scrollbar-thumb:hover {
  background: rgba(0, 255, 255, 0.5);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
  }
  
  .sidebar-collapsed {
    transform: translateX(-100%);
  }
  
  .sidebar:not(.sidebar-collapsed) {
    transform: translateX(0);
  }
  
  .main-content {
    margin-left: 0;
  }
  
  .main-content.expanded {
    margin-left: 0;
  }
  
  .content-header {
    padding: 15px 20px;
  }
  
  .content-body {
    padding: 15px;
  }
}
</style>