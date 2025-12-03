<template>
  <MainLayout>
    <div class="profile-view">
      <div class="profile-header">
        <h1>个人中心</h1>
      </div>
      
      <div class="profile-content">
        <div class="user-info">
          <div class="avatar">
            <img src="@/assets/avatar.png" alt="用户头像" v-if="currentUser?.avatar" />
            <div class="avatar-placeholder" v-else>
              {{ currentUser?.username?.charAt(0)?.toUpperCase() }}
            </div>
          </div>
          <div class="user-details">
            <h2>{{ currentUser?.username }}</h2>
            <p>{{ currentUser?.email }}</p>
            <p>注册时间: {{ formatDate(currentUser?.createdAt) }}</p>
          </div>
        </div>
        
        <div class="stats">
          <div class="stat-card">
            <h3>{{ userStats.musicCount }}</h3>
            <p>收藏音乐</p>
          </div>
          <div class="stat-card">
            <h3>{{ userStats.playlistCount }}</h3>
            <p>播放列表</p>
          </div>
          <div class="stat-card">
            <h3>{{ userStats.playTime }}</h3>
            <p>累计播放</p>
          </div>
        </div>
        
        <div class="settings">
          <h2>账户设置</h2>
          <form @submit.prevent="updateProfile">
            <div class="form-group">
              <label for="username">用户名</label>
              <input 
                type="text" 
                id="username" 
                v-model="profileForm.username"
                :placeholder="currentUser?.username"
              >
            </div>
            
            <div class="form-group">
              <label for="email">邮箱</label>
              <input 
                type="email" 
                id="email" 
                v-model="profileForm.email"
                :placeholder="currentUser?.email"
              >
            </div>
            
            <button type="submit" class="save-btn">保存更改</button>
          </form>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '@/store/modules/user'
import MainLayout from '@/components/layout/MainLayout.vue'

const userStore = useUserStore()

// 表单数据
const profileForm = ref({
  username: '',
  email: ''
})

// 计算属性
const currentUser = computed(() => userStore.currentUser)

const userStats = computed(() => ({
  musicCount: 128,
  playlistCount: 5,
  playTime: '24小时'
}))

// 方法
const updateProfile = () => {
  // 这里应该调用更新用户信息的API
  console.log('Updating profile:', profileForm.value)
  alert('个人信息更新成功！')
}

const formatDate = (dateString: string | undefined) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleDateString()
}

// 生命周期
onMounted(() => {
  // 初始化表单数据
  if (currentUser.value) {
    profileForm.value.username = currentUser.value.username
    profileForm.value.email = currentUser.value.email
  }
})
</script>

<style scoped>
.profile-view {
  max-width: 1200px;
  margin: 0 auto;
}

.profile-header h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.profile-content {
  background: white;
  border-radius: 8px;
  padding: 30px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #eee;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 20px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  background: #3498db;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 40px;
  font-weight: bold;
}

.user-details h2 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.user-details p {
  margin: 5px 0;
  color: #7f8c8d;
}

.stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.stat-card h3 {
  margin: 0 0 10px 0;
  color: #3498db;
  font-size: 24px;
}

.settings h2 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #2c3e50;
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 16px;
}

.save-btn {
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 12px 20px;
  cursor: pointer;
  font-size: 16px;
}
</style>