<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { crawlSingleSource, getSupportedSourceTypes } from '@/api/modules/crawler'
import { ElMessage } from 'element-plus'

const sourceUrl = ref('')
const sourceType = ref('general')
const crawlingStatus = ref('')
const musicResults = ref<any[]>([])
const supportedTypes = ref<string[]>([])
const isCrawling = ref(false)
const activeTab = ref('crawl')

// 测试用例 - 真实的音乐源URL
const testCases = ref([
  {
    name: 'YouTube音乐视频',
    url: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
    type: 'youtube'
  },
  {
    name: 'SoundCloud音频',
    url: 'https://soundcloud.com/imaginedragons/radioactive',
    type: 'soundcloud'
  },
  {
    name: '网易云音乐',
    url: 'https://music.163.com/#/song?id=186016',
    type: 'netease'
  }
])

// 内置测试歌曲
const builtInSongs = ref([
  {
    id: 1,
    title: 'Radioactive',
    artist: 'Imagine Dragons',
    url: 'https://example.com/music/radioactive.mp3',
    genre: 'Alternative Rock',
    mood: 'Energetic',
    quality: 'High'
  },
  {
    id: 2,
    title: 'Let Her Go',
    artist: 'Passenger',
    url: 'https://example.com/music/let-her-go.mp3',
    genre: 'Folk Rock',
    mood: 'Melancholic',
    quality: 'High'
  },
  {
    id: 3,
    title: 'Counting Stars',
    artist: 'OneRepublic',
    url: 'https://example.com/music/counting-stars.mp3',
    genre: 'Pop Rock',
    mood: 'Upbeat',
    quality: 'High'
  }
])

const loadSupportedTypes = async () => {
  try {
    const response = await getSupportedSourceTypes()
    if (response.code === 200) {
      supportedTypes.value = response.data
    } else {
      ElMessage.error('获取支持的音乐源类型失败')
    }
  } catch (error) {
    ElMessage.error('获取支持的音乐源类型失败')
  }
}

const startCrawling = async () => {
  if (!sourceUrl.value.trim()) {
    crawlingStatus.value = '请输入音乐源URL'
    return
  }
  
  isCrawling.value = true
  crawlingStatus.value = '正在爬取音乐源...'
  musicResults.value = []
  
  try {
    const response = await crawlSingleSource(sourceUrl.value, sourceType.value)
    if (response.code === 200) {
      musicResults.value = response.data
      crawlingStatus.value = `爬取完成，共找到 ${response.count} 首音乐`
      ElMessage.success(`爬取成功，共找到 ${response.count} 首音乐`)
    } else {
      crawlingStatus.value = '爬取失败: ' + response.message
      ElMessage.error('爬取失败: ' + response.message)
    }
  } catch (error: any) {
    crawlingStatus.value = '爬取失败: ' + (error.message || '网络错误')
    ElMessage.error('爬取失败: ' + (error.message || '网络错误'))
  } finally {
    isCrawling.value = false
  }
}

const useTestCase = (testCase: any) => {
  sourceUrl.value = testCase.url
  sourceType.value = testCase.type
}

const addToLibrary = (music: any) => {
  // 这里可以调用添加到音乐库的API
  ElMessage.success(`已将 "${music.title}" 添加到音乐库`)
}

onMounted(() => {
  loadSupportedTypes()
})
</script>

<template>
  <div class="music-crawler">
    <h1 class="high-tech-title">音乐源爬取</h1>
    
    <!-- Tab页 -->
    <div class="tab-container">
      <button 
        class="tab-button" 
        :class="{ 'active': activeTab === 'crawl' }"
        @click="activeTab = 'crawl'"
      >
        🔗 爬取音乐
      </button>
      <button 
        class="tab-button" 
        :class="{ 'active': activeTab === 'test' }"
        @click="activeTab = 'test'"
      >
        🧪 测试用例
      </button>
      <button 
        class="tab-button" 
        :class="{ 'active': activeTab === 'builtin' }"
        @click="activeTab = 'builtin'"
      >
        📦 内置歌曲
      </button>
    </div>
    
    <div class="high-tech-card">
      <!-- 爬取音乐Tab -->
      <div v-if="activeTab === 'crawl'">
        <div class="input-section">
          <label for="sourceUrl" class="input-label">音乐源URL:</label>
          <input 
            type="text" 
            id="sourceUrl" 
            v-model="sourceUrl" 
            class="input-field"
            placeholder="https://example.com/music"
          />
          
          <label for="sourceType" class="input-label">音乐源类型:</label>
          <select 
            id="sourceType" 
            v-model="sourceType" 
            class="input-field"
          >
            <option v-for="type in supportedTypes" :key="type" :value="type">
              {{ type }}
            </option>
          </select>
          
          <button 
            class="high-tech-button" 
            @click="startCrawling"
            :disabled="isCrawling"
          >
            🕷️ {{ isCrawling ? '爬取中...' : '开始爬取' }}
          </button>
        </div>
        
        <div class="status-section" v-if="crawlingStatus">
          <p class="status-text">{{ crawlingStatus }}</p>
        </div>
        
        <div class="results-section" v-if="musicResults.length > 0">
          <h2 class="card-title">爬取结果</h2>
          <div class="music-list">
            <div class="music-item" v-for="music in musicResults" :key="music.id">
              <div class="music-info">
                <h3>{{ music.title }}</h3>
                <p>{{ music.artist }}</p>
                <p class="music-url">{{ music.url }}</p>
              </div>
              <button 
                class="high-tech-button small" 
                @click="addToLibrary(music)"
              >
                ➕ 添加到库
              </button>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 测试用例Tab -->
      <div v-if="activeTab === 'test'">
        <h2 class="card-title">测试用例</h2>
        <div class="test-cases-list">
          <div class="test-case-item" v-for="(testCase, index) in testCases" :key="index">
            <div class="test-case-info">
              <h3>{{ testCase.name }}</h3>
              <p class="test-case-url">{{ testCase.url }}</p>
              <p class="test-case-type">类型: {{ testCase.type }}</p>
            </div>
            <button 
              class="high-tech-button small" 
              @click="useTestCase(testCase)"
            >
              🚀 使用此案例
            </button>
          </div>
        </div>
      </div>
      
      <!-- 内置歌曲Tab -->
      <div v-if="activeTab === 'builtin'">
        <h2 class="card-title">内置歌曲 (用于测试)</h2>
        <div class="music-list">
          <div class="music-item" v-for="music in builtInSongs" :key="music.id">
            <div class="music-info">
              <h3>{{ music.title }}</h3>
              <p>{{ music.artist }}</p>
              <p class="music-genre">🎼 {{ music.genre }}</p>
              <p class="music-mood">😊 {{ music.mood }}</p>
              <p class="music-quality">⭐ {{ music.quality }}</p>
            </div>
            <button 
              class="high-tech-button small" 
              @click="addToLibrary(music)"
            >
              ➕ 添加到库
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.music-crawler {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
}

/* Tab样式 */
.tab-container {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
}

.tab-button {
  padding: 10px 20px;
  background: rgba(26, 26, 46, 0.6);
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 10px 10px 0 0;
  color: #00ffff;
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button:hover,
.tab-button.active {
  background: rgba(0, 255, 255, 0.1);
  border-color: rgba(0, 255, 255, 0.8);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.input-section {
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

.status-section {
  margin-bottom: 30px;
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
}

.status-text {
  color: #00ffff;
  font-size: 16px;
  text-align: center;
}

.results-section {
  margin-top: 30px;
}

.music-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
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

.music-genre,
.music-mood,
.music-quality {
  font-size: 14px;
  color: rgba(0, 255, 255, 0.8);
  margin: 5px 0;
}

/* 测试用例样式 */
.test-cases-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.test-case-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  transition: all 0.3s ease;
}

.test-case-item:hover {
  border-color: rgba(0, 255, 255, 0.5);
  box-shadow: 0 0 20px rgba(0, 255, 255, 0.1);
}

.test-case-info h3 {
  color: #00ffff;
  margin: 0 0 10px 0;
  font-size: 18px;
}

.test-case-url {
  color: rgba(0, 255, 255, 0.7);
  font-size: 14px;
  margin: 0 0 5px 0;
  word-break: break-all;
}

.test-case-type {
  color: rgba(0, 255, 255, 0.8);
  font-size: 14px;
  margin: 0;
}

.music-item:hover {
  border-color: rgba(0, 255, 255, 0.5);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.2);
}

.music-info h3 {
  color: #00ffff;
  font-size: 18px;
  margin-bottom: 5px;
}

.music-info p {
  color: rgba(0, 255, 255, 0.7);
  font-size: 14px;
  margin-bottom: 3px;
}

.music-url {
  font-size: 12px;
  color: rgba(0, 255, 255, 0.5);
  word-break: break-all;
}

.small {
  padding: 8px 20px;
  font-size: 14px;
}
</style>