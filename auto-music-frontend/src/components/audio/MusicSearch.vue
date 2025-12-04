<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { searchMusic } from '@/api/modules/crawler'
import { ElMessage, ElPagination } from 'element-plus'
import { Music } from '@/types/models/music'

const searchKeyword = ref('')
const searchResults = ref<Music[]>([])
const totalResults = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const isSearching = ref(false)

const handleSearch = async () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.error('请输入搜索关键词')
    return
  }

  isSearching.value = true
  currentPage.value = 1

  try {
    const response = await searchMusic(searchKeyword.value, currentPage.value, pageSize.value)
    if (response.code === 200) {
      searchResults.value = response.data.items
      totalResults.value = response.data.total
      ElMessage.success(`搜索成功，共找到 ${response.data.total} 首音乐`)
    } else {
      ElMessage.error('搜索失败: ' + response.message)
    }
  } catch (error: any) {
    ElMessage.error('搜索失败: ' + (error.message || '网络错误'))
  } finally {
    isSearching.value = false
  }
}

const handlePageChange = async (page: number) => {
  currentPage.value = page
  await handleSearch()
}

const addToLibrary = (music: Music) => {
  // 这里可以调用添加到音乐库的API
  ElMessage.success(`已将 "${music.title}" 添加到音乐库`)
}

onMounted(() => {
  // 页面加载时可以执行默认搜索
  // handleSearch()
})
</script>

<template>
  <div class="music-search">
    <h1 class="high-tech-title">音乐搜索</h1>
    
    <div class="high-tech-card">
      <div class="search-section">
        <label for="searchKeyword" class="input-label">搜索关键词:</label>
        <input 
          type="text" 
          id="searchKeyword" 
          v-model="searchKeyword" 
          class="input-field"
          placeholder="输入音乐标题、艺术家或关键词"
          @keyup.enter="handleSearch"
        />
        
        <button 
          class="high-tech-button" 
          @click="handleSearch"
          :disabled="isSearching"
        >
          🔍 {{ isSearching ? '搜索中...' : '搜索' }}
        </button>
      </div>
      
      <div class="results-section" v-if="searchResults.length > 0">
        <h2 class="card-title">搜索结果</h2>
        <div class="music-list">
          <div class="music-item" v-for="music in searchResults" :key="music.id">
            <div class="music-info">
              <h3>{{ music.title }}</h3>
              <p>{{ music.artist }}</p>
              <p>⏱️ {{ music.duration }} 秒</p>
              <p>🎼 音质: {{ music.quality }}</p>
            </div>
            <button 
              class="high-tech-button small" 
              @click="addToLibrary(music)"
            >
              ➕ 添加到库
            </button>
          </div>
        </div>
        
        <ElPagination
          class="pagination"
          background
          layout="prev, pager, next, jumper, ->, total"
          :total="totalResults"
          :current-page="currentPage"
          :page-size="pageSize"
          @current-change="handlePageChange"
        />
      </div>
      
      <div class="no-results" v-if="searchResults.length === 0 && !isSearching && searchKeyword.value.trim()">
        <p>未找到相关音乐</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.music-search {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.search-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 30px;
}

.input-label {
  font-family: 'Rajdhani', sans-serif;
  font-size: 18px;
  color: #00ffff;
  font-weight: 600;
}

.input-field {
  padding: 12px 20px;
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 10px;
  background: rgba(26, 26, 46, 0.6);
  color: #00ffff;
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  outline: none;
  transition: all 0.3s ease;
}

.input-field:focus {
  border-color: rgba(0, 255, 255, 0.8);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.input-field::placeholder {
  color: rgba(0, 255, 255, 0.5);
}

.results-section {
  margin-top: 30px;
}

.music-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-bottom: 30px;
}

.music-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.music-item:hover {
  border-color: rgba(0, 255, 255, 0.8);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.music-info {
  color: #00ffff;
}

.music-info h3 {
  margin: 0 0 10px 0;
  font-size: 20px;
}

.music-info p {
  margin: 5px 0;
  font-size: 16px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 30px;
}

.no-results {
  margin-top: 30px;
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  color: #00ffff;
  text-align: center;
}
</style>