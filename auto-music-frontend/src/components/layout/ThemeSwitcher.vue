<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElSelect, ElOption } from 'element-plus'
import { setTheme, getTheme } from '@/api/modules/theme'
import { Theme } from '@/types/models/theme'

const currentTheme = ref('default')
const availableThemes = ref<Theme[]>([])
const isLoading = ref(false)

const loadThemes = async () => {
  try {
    const response = await getTheme()
    if (response.code === 200) {
      currentTheme.value = response.data
    } else {
      ElMessage.error('获取当前主题失败')
    }
  } catch (error: any) {
    ElMessage.error('获取当前主题失败: ' + (error.message || '网络错误'))
  }
}

const handleThemeChange = async (theme: string) => {
  isLoading.value = true

  try {
    const response = await setTheme(theme)
    if (response.code === 200) {
      currentTheme.value = theme
      ElMessage.success('主题切换成功')
      // 这里可以添加主题切换的逻辑
      applyTheme(theme)
    } else {
      ElMessage.error('主题切换失败: ' + response.message)
    }
  } catch (error: any) {
    ElMessage.error('主题切换失败: ' + (error.message || '网络错误'))
  } finally {
    isLoading.value = false
  }
}

const applyTheme = (theme: string) => {
  // 这里可以添加主题切换的逻辑
  // 例如，修改body的类名
  document.body.className = `theme-${theme}`
}

onMounted(() => {
  loadThemes()
  // 初始化主题
  applyTheme(currentTheme.value)
})
</script>

<template>
  <div class="theme-switcher">
    <label for="themeSelect" class="input-label">选择主题:</label>
    <ElSelect
      id="themeSelect"
      v-model="currentTheme"
      placeholder="请选择"
      size="small"
      @change="handleThemeChange"
      :loading="isLoading"
    >
      <ElOption label="默认主题" value="default" />
      <ElOption label="黑暗主题" value="dark" />
      <ElOption label="明亮主题" value="light" />
      <ElOption label="科技主题" value="tech" />
      <ElOption label="复古主题" value="vintage" />
    </ElSelect>
  </div>
</template>

<style scoped>
.theme-switcher {
  display: flex;
  align-items: center;
  gap: 10px;
}

.input-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  color: #00ffff;
  font-weight: 600;
}
</style>