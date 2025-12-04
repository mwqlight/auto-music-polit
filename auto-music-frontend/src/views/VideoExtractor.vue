<template>
  <div class="video-extractor-container">
    <div class="header">
      <h1>视频音乐提取器</h1>
      <p>上传视频文件或输入视频链接，提取其中的音频</p>
    </div>

    <div class="main-content">
      <!-- 文件上传标签页 -->
      <div class="tab-content" v-show="activeTab === 'upload'">
        <div class="upload-area" @click="triggerFileInput" @dragover.prevent @drop="handleDrop">
          <input 
            ref="fileInput" 
            type="file" 
            accept="video/*" 
            @change="handleFileSelect" 
            style="display: none"
          >
          <div class="upload-icon">📤</div>
          <div class="upload-text">点击或拖拽视频文件到此处</div>
          <div class="upload-hint">支持 MP4, AVI, MOV, MKV 等格式</div>
        </div>

        <div class="file-info" v-if="selectedFile">
          <div class="file-name">{{ selectedFile.name }}</div>
          <div class="file-size">{{ formatFileSize(selectedFile.size) }}</div>
          <button class="remove-btn" @click="removeFile">✕</button>
        </div>

        <button 
          class="extract-btn" 
          :disabled="!selectedFile || isProcessing" 
          @click="extractFromFile"
        >
          {{ isProcessing ? '提取中...' : '提取音频' }}
        </button>
      </div>

      <!-- URL提取标签页 -->
      <div class="tab-content" v-show="activeTab === 'url'">
        <div class="url-input-container">
          <input 
            type="text" 
            v-model="videoUrl" 
            placeholder="输入视频链接（支持 YouTube, Bilibili, 抖音等）" 
            class="url-input"
          >
        </div>

        <button 
          class="extract-btn" 
          :disabled="!videoUrl.trim() || isProcessing" 
          @click="extractFromUrl"
        >
          {{ isProcessing ? '提取中...' : '提取音频' }}
        </button>
      </div>

      <!-- 进度显示 -->
      <div class="progress-container" v-if="isProcessing">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: progress + '%' }"></div>
        </div>
        <div class="progress-text">{{ progressText }}</div>
      </div>

      <!-- 结果显示 -->
      <div class="result-container" v-if="extractedAudio">
        <div class="result-header">
          <h3>提取成功！</h3>
          <button class="play-btn" @click="playAudio">▶</button>
        </div>
        <div class="audio-info">
          <div class="audio-name">{{ extractedAudio.name }}</div>
          <div class="audio-duration">{{ formatDuration(extractedAudio.duration) }}</div>
        </div>
        <div class="audio-controls">
          <button class="control-btn" @click="downloadAudio">💾 下载</button>
          <button class="control-btn" @click="addToLibrary">➕ 添加到音乐库</button>
        </div>
      </div>

      <!-- 错误提示 -->
      <div class="error-message" v-if="errorMessage">
        <div class="error-icon">❌</div>
        <div class="error-text">{{ errorMessage }}</div>
        <button class="error-close" @click="clearError">✕</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { uploadVideo, extractAudioFromUrl } from '@/api/modules/crawler'

const activeTab = ref('upload')
const selectedFile = ref<File | null>(null)
const videoUrl = ref('')
const isProcessing = ref(false)
const progress = ref(0)
const progressText = ref('')
const extractedAudio = ref<any>(null)
const errorMessage = ref('')
const fileInput = ref<HTMLInputElement | null>(null)

const triggerFileInput = () => {
  fileInput.value?.click()
}

const handleFileSelect = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    selectedFile.value = target.files[0]
    clearError()
  }
}

const handleDrop = (event: DragEvent) => {
  event.preventDefault()
  if (event.dataTransfer && event.dataTransfer.files.length > 0) {
    selectedFile.value = event.dataTransfer.files[0]
    clearError()
  }
}

const removeFile = () => {
  selectedFile.value = null
  fileInput.value?.value && (fileInput.value.value = '')
}

const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatDuration = (seconds: number): string => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

const clearError = () => {
  errorMessage.value = ''
}

const showError = (message: string) => {
  errorMessage.value = message
  isProcessing.value = false
  progress.value = 0
  progressText.value = ''
}

const extractFromFile = async () => {
  if (!selectedFile.value) return
  
  isProcessing.value = true
  progress.value = 0
  progressText.value = '正在上传视频文件...'
  extractedAudio.value = null
  clearError()

  try {
    const formData = new FormData()
    formData.append('video', selectedFile.value)

    const response = await uploadVideo(formData, (event) => {
      if (event.loaded && event.total) {
        progress.value = Math.round((event.loaded / event.total) * 100)
        progressText.value = `上传中... ${progress.value}%`
      }
    })

    if (response.code === 200) {
      extractedAudio.value = response.data
      progressText.value = '提取完成！'
    } else {
      showError(response.message || '提取失败')
    }
  } catch (error: any) {
    showError(error.message || '网络错误，请重试')
  } finally {
    isProcessing.value = false
  }
}

const extractFromUrl = async () => {
  if (!videoUrl.value.trim()) return
  
  isProcessing.value = true
  progress.value = 0
  progressText.value = '正在分析视频链接...'
  extractedAudio.value = null
  clearError()

  try {
    const response = await extractAudioFromUrl(videoUrl.value, (event) => {
      if (event.loaded && event.total) {
        progress.value = Math.round((event.loaded / event.total) * 100)
        progressText.value = `处理中... ${progress.value}%`
      }
    })

    if (response.code === 200) {
      extractedAudio.value = response.data
      progressText.value = '提取完成！'
    } else {
      showError(response.message || '提取失败')
    }
  } catch (error: any) {
    showError(error.message || '网络错误，请重试')
  } finally {
    isProcessing.value = false
  }
}

const playAudio = () => {
  if (extractedAudio.value && extractedAudio.value.url) {
    const audio = new Audio(extractedAudio.value.url)
    audio.play()
  }
}

const downloadAudio = () => {
  if (extractedAudio.value && extractedAudio.value.url) {
    const link = document.createElement('a')
    link.href = extractedAudio.value.url
    link.download = extractedAudio.value.name || 'extracted_audio.mp3'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }
}

const addToLibrary = () => {
  if (extractedAudio.value) {
    // 这里可以添加到音乐库的逻辑
    alert('已添加到音乐库！')
  }
}
</script>

<style scoped>
.video-extractor-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 20px;
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.header h1 {
  font-size: 2.5rem;
  font-weight: 700;
  color: #00ffff;
  margin-bottom: 10px;
  text-shadow: 0 0 20px rgba(0, 255, 255, 0.5);
}

.header p {
  font-size: 1.1rem;
  color: #888;
}

.main-content {
  background: rgba(26, 26, 46, 0.8);
  border-radius: 20px;
  padding: 30px;
  box-shadow: 0 0 30px rgba(0, 255, 255, 0.1);
}

.tab-content {
  margin-bottom: 20px;
}

.upload-area {
  border: 2px dashed #00ffff;
  border-radius: 15px;
  padding: 60px 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: rgba(0, 255, 255, 0.05);
}

.upload-area:hover {
  border-color: #0080ff;
  background: rgba(0, 128, 255, 0.05);
}

.upload-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.upload-text {
  font-size: 1.3rem;
  font-weight: 500;
  color: #00ffff;
  margin-bottom: 10px;
}

.upload-hint {
  font-size: 0.9rem;
  color: #888;
}

.file-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 10px;
}

.file-name {
  font-weight: 500;
  color: #fff;
}

.file-size {
  font-size: 0.9rem;
  color: #888;
}

.remove-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #ff4444;
  padding: 5px 10px;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.remove-btn:hover {
  background: rgba(255, 68, 68, 0.2);
}

.url-input-container {
  margin-bottom: 20px;
}

.url-input {
  width: 100%;
  padding: 15px 20px;
  border: 2px solid #00ffff;
  border-radius: 10px;
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
  font-size: 1rem;
  outline: none;
  transition: all 0.3s ease;
}

.url-input:focus {
  border-color: #0080ff;
  background: rgba(255, 255, 255, 0.1);
}

.url-input::placeholder {
  color: #888;
}

.extract-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(45deg, #00ffff, #0080ff);
  border: none;
  border-radius: 10px;
  font-size: 1.2rem;
  font-weight: 700;
  color: #0a0e27;
  cursor: pointer;
  transition: all 0.3s ease;
  text-transform: uppercase;
}

.extract-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 20px rgba(0, 255, 255, 0.3);
}

.extract-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.progress-container {
  margin-top: 20px;
}

.progress-bar {
  width: 100%;
  height: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 5px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #00ffff, #0080ff);
  transition: width 0.3s ease;
}

.progress-text {
  text-align: center;
  margin-top: 10px;
  font-size: 0.9rem;
  color: #00ffff;
}

.result-container {
  margin-top: 30px;
  padding: 20px;
  background: rgba(0, 255, 255, 0.05);
  border-radius: 15px;
  border: 2px solid #00ffff;
}

.result-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 15px;
}

.result-header h3 {
  color: #00ffff;
  font-size: 1.5rem;
}

.play-btn {
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: #00ffff;
  transition: transform 0.3s ease;
}

.play-btn:hover {
  transform: scale(1.2);
}

.audio-info {
  margin-bottom: 20px;
}

.audio-name {
  font-size: 1.2rem;
  font-weight: 500;
  color: #fff;
  margin-bottom: 5px;
}

.audio-duration {
  font-size: 0.9rem;
  color: #888;
}

.audio-controls {
  display: flex;
  gap: 10px;
}

.control-btn {
  flex: 1;
  padding: 12px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid #00ffff;
  border-radius: 8px;
  color: #00ffff;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.control-btn:hover {
  background: rgba(0, 255, 255, 0.2);
  border-color: #0080ff;
}

.error-message {
  margin-top: 20px;
  padding: 15px;
  background: rgba(255, 68, 68, 0.1);
  border: 2px solid #ff4444;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.error-icon {
  font-size: 1.5rem;
  margin-right: 10px;
}

.error-text {
  flex: 1;
  color: #ff4444;
}

.error-close {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
  color: #ff4444;
  padding: 5px 10px;
  border-radius: 50%;
  transition: background 0.3s ease;
}

.error-close:hover {
  background: rgba(255, 68, 68, 0.2);
}
</style>