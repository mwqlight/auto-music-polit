<template>
  <MainLayout>
    <div class="music-library-view">
      <div class="library-header">
        <h1>音乐库</h1>
      </div>
      
      <!-- 音乐采集部分 -->
      <div class="crawler-section">
        <h2>音乐采集中枢</h2>
        <div class="crawler-form">
          <input 
            v-model="sourceUrl" 
            type="text" 
            placeholder="输入音乐源URL" 
            class="url-input"
          />
          <button @click="crawlMusic" :disabled="loading" class="crawl-btn">
            {{ loading ? '爬取中...' : '爬取音乐' }}
          </button>
        </div>
        
        <div class="crawler-actions">
          <button @click="executeDailyCrawl" :disabled="loading" class="daily-crawl-btn">
            执行每日爬取
          </button>
        </div>
        
        <div v-if="resultMessage" class="result-message">
          {{ resultMessage }}
        </div>
      </div>
      
      <!-- 音乐列表部分 -->
      <div class="music-list-section">
        <h2>音乐列表</h2>
        <MusicList />
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import MainLayout from '@/components/layout/MainLayout.vue'
import MusicList from '@/components/audio/MusicList.vue'
import { crawler } from '@/api/modules'

const sourceUrl = ref('')
const loading = ref(false)
const resultMessage = ref('')

// 从指定源URL爬取音乐
const crawlMusic = async () => {
  if (!sourceUrl.value.trim()) {
    resultMessage.value = '请输入音乐源URL'
    return
  }
  
  loading.value = true
  resultMessage.value = ''
  
  try {
    const response = await crawler.crawlMusicFromSource(sourceUrl.value)
    resultMessage.value = `成功爬取到 ${response.data.length} 首音乐`
    sourceUrl.value = ''
  } catch (error) {
    resultMessage.value = '爬取音乐失败，请稍后重试'
    console.error('爬取音乐失败:', error)
  } finally {
    loading.value = false
  }
}

// 执行每日爬取任务
const executeDailyCrawl = async () => {
  loading.value = true
  resultMessage.value = ''
  
  try {
    await crawler.executeDailyCrawlingJob()
    resultMessage.value = '每日爬取任务已执行'
  } catch (error) {
    resultMessage.value = '执行每日爬取任务失败，请稍后重试'
    console.error('执行每日爬取任务失败:', error)
  } finally {
    loading.value = false
  }
}
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

.crawler-section {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 30px;
}

.crawler-section h2 {
  color: #2c3e50;
  margin-bottom: 15px;
}

.crawler-form {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.url-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.crawl-btn {
  padding: 10px 20px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.crawl-btn:hover:not(:disabled) {
  background: #0056b3;
}

.crawl-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.crawler-actions {
  margin-bottom: 15px;
}

.daily-crawl-btn {
  padding: 10px 20px;
  background: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.daily-crawl-btn:hover:not(:disabled) {
  background: #218838;
}

.daily-crawl-btn:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.result-message {
  padding: 10px;
  border-radius: 4px;
  font-size: 14px;
}

.result-message.success {
  background: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.result-message.error {
  background: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.music-list-section h2 {
  color: #2c3e50;
  margin-bottom: 20px;
}
</style>