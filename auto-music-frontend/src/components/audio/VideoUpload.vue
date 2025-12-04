<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage, ElUpload, ElButton, ElProgress } from 'element-plus'
import { uploadVideo, extractAudioFromUrl } from '@/api/modules/crawler'
import { Music } from '@/types/models/music'

const videoFile = ref<File | null>(null)
const videoUrl = ref('')
const isUploading = ref(false)
const isExtracting = ref(false)
const uploadProgress = ref(0)
const extractProgress = ref(0)
const extractedMusic = ref<Music | null>(null)
const activeTab = ref('file')

const handleFileChange = (file: File) => {
  videoFile.value = file
}

const handleUpload = async () => {
  if (!videoFile.value) {
    ElMessage.error('请选择视频文件')
    return
  }

  isUploading.value = true
  uploadProgress.value = 0

  try {
    const formData = new FormData()
    formData.append('video', videoFile.value)

    const response = await uploadVideo(formData, (progressEvent) => {
      if (progressEvent.total) {
        uploadProgress.value = Math.round((progressEvent.loaded / progressEvent.total) * 100)
      }
    })

    if (response.code === 200) {
      extractedMusic.value = response.data
      ElMessage.success('音频提取成功')
    } else {
      ElMessage.error('音频提取失败: ' + response.message)
    }
  } catch (error: any) {
    ElMessage.error('音频提取失败: ' + (error.message || '网络错误'))
  } finally {
    isUploading.value = false
  }
}

const handleUrlExtract = async () => {
  if (!videoUrl.value.trim()) {
    ElMessage.error('请输入视频URL')
    return
  }

  isExtracting.value = true
  extractProgress.value = 0

  try {
    const response = await extractAudioFromUrl(videoUrl.value, (progressEvent) => {
      if (progressEvent.total) {
        extractProgress.value = Math.round((progressEvent.loaded / progressEvent.total) * 100)
      }
    })

    if (response.code === 200) {
      extractedMusic.value = response.data
      ElMessage.success('音频提取成功')
    } else {
      ElMessage.error('音频提取失败: ' + response.message)
    }
  } catch (error: any) {
    ElMessage.error('音频提取失败: ' + (error.message || '网络错误'))
  } finally {
    isExtracting.value = false
  }
}

const addToLibrary = () => {
  if (extractedMusic.value) {
    // 这里可以调用添加到音乐库的API
    ElMessage.success(`已将 "${extractedMusic.value.title}" 添加到音乐库`)
  }
}
</script>

<template>
  <div class="video-upload">
    <h1 class="high-tech-title">视频音频提取</h1>
    
    <div class="high-tech-card">
      <!-- 选项卡切换 -->
      <div class="tabs">
        <button 
          class="tab-button" 
          :class="{ active: activeTab === 'file' }"
          @click="activeTab = 'file'"
        >
          📁 上传视频文件
        </button>
        <button 
          class="tab-button" 
          :class="{ active: activeTab === 'url' }"
          @click="activeTab = 'url'"
        >
          🔗 从视频URL提取
        </button>
      </div>

      <!-- 文件上传 -->
      <div v-if="activeTab === 'file'" class="upload-section">
        <ElUpload
          class="uploader"
          action=""
          :auto-upload="false"
          :show-file-list="false"
          :before-upload="() => false"
          :on-change="handleFileChange"
        >
          <ElButton type="primary" size="large">
            📂 选择视频文件
          </ElButton>
        </ElUpload>
        
        <div v-if="videoFile" class="file-info">
          <p>📄 {{ videoFile.name }}</p>
          <p>📊 {{ (videoFile.size / 1024 / 1024).toFixed(2) }} MB</p>
        </div>
        
        <button 
          class="high-tech-button" 
          @click="handleUpload"
          :disabled="!videoFile || isUploading"
        >
          🎵 {{ isUploading ? '提取中...' : '提取音频' }}
        </button>
        
        <ElProgress 
          v-if="isUploading" 
          :percentage="uploadProgress" 
          status="success"
          class="progress"
        />
      </div>

      <!-- URL提取 -->
      <div v-if="activeTab === 'url'" class="url-section">
        <label for="videoUrl" class="input-label">视频URL:</label>
        <input 
          type="text" 
          id="videoUrl" 
          v-model="videoUrl" 
          class="input-field"
          placeholder="https://example.com/video.mp4"
        />
        
        <button 
          class="high-tech-button" 
          @click="handleUrlExtract"
          :disabled="!videoUrl.trim() || isExtracting"
        >
          🎵 {{ isExtracting ? '提取中...' : '提取音频' }}
        </button>
        
        <ElProgress 
          v-if="isExtracting" 
          :percentage="extractProgress" 
          status="success"
          class="progress"
        />
      </div>

      <!-- 提取结果 -->
      <div v-if="extractedMusic" class="results-section">
        <h2 class="card-title">提取结果</h2>
        <div class="music-info">
          <h3>{{ extractedMusic.title }}</h3>
          <p>{{ extractedMusic.artist }}</p>
          <p>⏱️ {{ extractedMusic.duration }} 秒</p>
          <p>🎼 音质: {{ extractedMusic.quality }}</p>
        </div>
        <button 
          class="high-tech-button small" 
          @click="addToLibrary"
        >
          ➕ 添加到库
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.video-upload {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.tabs {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
}

.tab-button {
  padding: 10px 20px;
  border: 1px solid rgba(0, 255, 255, 0.3);
  border-radius: 10px;
  background: rgba(26, 26, 46, 0.6);
  color: #00ffff;
  font-family: 'Rajdhani', sans-serif;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.tab-button:hover {
  border-color: rgba(0, 255, 255, 0.8);
  box-shadow: 0 0 15px rgba(0, 255, 255, 0.3);
}

.tab-button.active {
  background: rgba(0, 255, 255, 0.1);
  border-color: rgba(0, 255, 255, 0.8);
}

.upload-section,
.url-section {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.uploader {
  margin-bottom: 15px;
}

.file-info {
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
  color: #00ffff;
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

.progress {
  margin-top: 15px;
}

.results-section {
  margin-top: 30px;
  padding: 15px;
  background: rgba(26, 26, 46, 0.6);
  border-radius: 10px;
  border: 1px solid rgba(0, 255, 255, 0.2);
}

.music-info {
  margin-bottom: 15px;
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
</style>