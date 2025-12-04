<template>
  <MainLayout>
    <div class="ai-music-view">
      <div class="page-header">
        <h1>量子音创工坊</h1>
        <p>AI驱动的音乐创作与识别平台</p>
      </div>

      <div class="ai-features">
        <!-- 音乐识别 -->
        <div class="feature-section">
          <h2>🎧 听音辩曲大师</h2>
          <div class="feature-content">
            <div class="upload-area">
              <div 
                class="drop-zone"
                :class="{ 'drag-over': isDragOver }"
                @dragover.prevent="handleDragOver"
                @dragleave.prevent="handleDragLeave"
                @drop.prevent="handleDrop('recognize', $event)"
              >
                <div class="upload-content">
                  <MusicIcon class="upload-icon" />
                  <p>拖拽音频文件到这里或点击上传</p>
                  <p class="file-types">支持 MP3, WAV, FLAC 格式</p>
                  <input 
                    type="file" 
                    ref="recognizeFileInput"
                    accept="audio/*"
                    @change="handleFileSelect('recognize', $event)"
                    style="display: none;"
                  >
                  <button 
                    class="upload-btn"
                    @click="$refs.recognizeFileInput.click()"
                    :disabled="isRecognizing"
                  >
                    {{ isRecognizing ? '识别中...' : '选择音频文件' }}
                  </button>
                </div>
              </div>
            </div>

            <button 
              class="recognize-btn"
              @click="recognizeMusic"
              :disabled="!selectedRecognizeFile || isRecognizing"
            >
              {{ isRecognizing ? '正在识别...' : '开始识别音乐' }}
            </button>

            <div v-if="recognitionResult" class="recognition-result">
              <h3>识别结果</h3>
              <pre>{{ recognitionResult }}</pre>
            </div>
          </div>
        </div>

        <!-- AI音乐生成 -->
        <div class="feature-section">
          <h2>🎵 AI智能编曲</h2>
          <div class="feature-content">
            <div class="form-group">
              <label>音乐风格:</label>
              <select v-model="generateParams.style">
                <option value="流行">流行</option>
                <option value="古典">古典</option>
                <option value="摇滚">摇滚</option>
                <option value="爵士">爵士</option>
                <option value="电子">电子</option>
                <option value="乡村">乡村</option>
                <option value="嘻哈">嘻哈</option>
                <option value="新世纪">新世纪</option>
              </select>
            </div>

            <div class="form-group">
              <label>音乐情绪:</label>
              <select v-model="generateParams.mood">
                <option value="欢快">欢快</option>
                <option value="忧郁">忧郁</option>
                <option value="宁静">宁静</option>
                <option value="激情">激情</option>
                <option value="浪漫">浪漫</option>
                <option value="神秘">神秘</option>
                <option value="史诗">史诗</option>
                <option value="梦幻">梦幻</option>
              </select>
            </div>

            <div class="form-group">
              <label>音乐时长: {{ generateParams.duration }} 秒</label>
              <input 
                type="range" 
                min="10" 
                max="300" 
                v-model.number="generateParams.duration"
              >
            </div>

            <button 
              class="generate-btn"
              @click="generateMusic"
              :disabled="isGenerating"
            >
              {{ isGenerating ? '生成中...' : '生成AI音乐' }}
            </button>

            <div v-if="generatedMusicUrl" class="playback-section">
              <h3>生成的音乐</h3>
              <audio :src="generatedMusicUrl" controls></audio>
              <button @click="downloadGeneratedMusic" class="download-btn">
                下载音乐
              </button>
            </div>
          </div>
        </div>

        <!-- AI音乐混音 -->
        <div class="feature-section">
          <h2>🎛️ AI智能混音</h2>
          <div class="feature-content">
            <div class="upload-area">
              <div 
                class="drop-zone"
                :class="{ 'drag-over': isRemixDragOver }"
                @dragover.prevent="handleRemixDragOver"
                @dragleave.prevent="handleRemixDragLeave"
                @drop.prevent="handleDrop('remix', $event)"
              >
                <div class="upload-content">
                  <MusicIcon class="upload-icon" />
                  <p>拖拽音频文件到这里或点击上传</p>
                  <p class="file-types">支持 MP3, WAV, FLAC 格式</p>
                  <input 
                    type="file" 
                    ref="remixFileInput"
                    accept="audio/*"
                    @change="handleFileSelect('remix', $event)"
                    style="display: none;"
                  >
                  <button 
                    class="upload-btn"
                    @click="$refs.remixFileInput.click()"
                    :disabled="isRemixing"
                  >
                    {{ isRemixing ? '处理中...' : '选择音频文件' }}
                  </button>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label>混音风格:</label>
              <select v-model="remixStyle">
                <option value="流行">流行</option>
                <option value="古典">古典</option>
                <option value="摇滚">摇滚</option>
                <option value="爵士">爵士</option>
                <option value="电子">电子</option>
                <option value="乡村">乡村</option>
                <option value="嘻哈">嘻哈</option>
                <option value="新世纪">新世纪</option>
              </select>
            </div>

            <button 
              class="remix-btn"
              @click="remixMusic"
              :disabled="!selectedRemixFile || !remixStyle || isRemixing"
            >
              {{ isRemixing ? '混音中...' : '开始AI混音' }}
            </button>

            <div v-if="remixedMusicUrl" class="playback-section">
              <h3>混音后的音乐</h3>
              <audio :src="remixedMusicUrl" controls></audio>
              <button @click="downloadRemixedMusic" class="download-btn">
                下载音乐
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { recognizeMusic, generateMusic, remixMusic } from '@/api/modules/aiMusic'
import MainLayout from '@/components/layout/MainLayout.vue'
import { Music as MusicIcon } from 'lucide-vue-next'

// 音乐识别相关
const isDragOver = ref(false)
const isRemixDragOver = ref(false)
const selectedRecognizeFile = ref<File | null>(null)
const selectedRemixFile = ref<File | null>(null)
const isRecognizing = ref(false)
const recognitionResult = ref('')
const isGenerating = ref(false)
const isRemixing = ref(false)
const generatedMusicUrl = ref('')
const remixedMusicUrl = ref('')
const remixStyle = ref('流行')

// AI音乐生成参数
const generateParams = reactive({
  style: '流行',
  mood: '欢快',
  duration: 60
})

// 文件拖拽处理
const handleDragOver = () => {
  isDragOver.value = true
}

const handleDragLeave = () => {
  isDragOver.value = false
}

const handleRemixDragOver = () => {
  isRemixDragOver.value = true
}

const handleRemixDragLeave = () => {
  isRemixDragOver.value = false
}

// 文件选择处理
const handleFileSelect = (type: string, event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.files && target.files.length > 0) {
    if (type === 'recognize') {
      selectedRecognizeFile.value = target.files[0]
    } else if (type === 'remix') {
      selectedRemixFile.value = target.files[0]
    }
  }
}

// 文件拖拽处理
const handleDrop = (type: string, event: DragEvent) => {
  isDragOver.value = false
  isRemixDragOver.value = false
  
  if (event.dataTransfer && event.dataTransfer.files.length > 0) {
    const file = event.dataTransfer.files[0]
    if (type === 'recognize') {
      selectedRecognizeFile.value = file
    } else if (type === 'remix') {
      selectedRemixFile.value = file
    }
  }
}

// 音乐识别
const recognizeMusicHandler = async () => {
  if (!selectedRecognizeFile.value) return
  
  isRecognizing.value = true
  recognitionResult.value = ''
  
  try {
    const result = await recognizeMusic(selectedRecognizeFile.value)
    recognitionResult.value = result
  } catch (error) {
    recognitionResult.value = '识别失败: ' + (error as Error).message
  } finally {
    isRecognizing.value = false
  }
}

// AI音乐生成
const generateMusicHandler = async () => {
  isGenerating.value = true
  generatedMusicUrl.value = ''
  
  try {
    const blob = await generateMusic({
      style: generateParams.style,
      mood: generateParams.mood,
      duration: generateParams.duration
    })
    
    generatedMusicUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    console.error('音乐生成失败:', error)
  } finally {
    isGenerating.value = false
  }
}

// AI音乐混音
const remixMusicHandler = async () => {
  if (!selectedRemixFile.value || !remixStyle.value) return
  
  isRemixing.value = true
  remixedMusicUrl.value = ''
  
  try {
    const blob = await remixMusic(selectedRemixFile.value, remixStyle.value)
    remixedMusicUrl.value = URL.createObjectURL(blob)
  } catch (error) {
    console.error('音乐混音失败:', error)
  } finally {
    isRemixing.value = false
  }
}

// 下载生成的音乐
const downloadGeneratedMusic = () => {
  if (generatedMusicUrl.value) {
    const a = document.createElement('a')
    a.href = generatedMusicUrl.value
    a.download = 'generated-music.wav'
    a.click()
  }
}

// 下载混音后的音乐
const downloadRemixedMusic = () => {
  if (remixedMusicUrl.value) {
    const a = document.createElement('a')
    a.href = remixedMusicUrl.value
    a.download = 'remixed-music.wav'
    a.click()
  }
}
</script>

<style scoped>
.ai-music-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a2e 50%, #16213e 100%);
  min-height: 100vh;
}

.page-header h1 {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #00d4ff;
  text-align: center;
  text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
  background: linear-gradient(45deg, #00d4ff, #ff00ff);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.page-header p {
  color: #00ff9d;
  margin-bottom: 3rem;
  text-align: center;
  font-size: 1.1rem;
  text-shadow: 0 0 10px rgba(0, 255, 157, 0.3);
}

.ai-features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.feature-section {
  background: rgba(26, 26, 46, 0.8);
  border-radius: 12px;
  padding: 2rem;
  border: 1px solid rgba(0, 212, 255, 0.2);
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.1);
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

.feature-section:hover {
  transform: translateY(-5px);
  box-shadow: 0 0 50px rgba(0, 212, 255, 0.2);
  border-color: rgba(0, 212, 255, 0.5);
}

.feature-section h2 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: #00d4ff;
  text-shadow: 0 0 10px rgba(0, 212, 255, 0.5);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #00ff9d;
  text-shadow: 0 0 5px rgba(0, 255, 157, 0.3);
}

.form-group select, .form-group input[type="range"] {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid rgba(0, 212, 255, 0.3);
  background: rgba(10, 10, 10, 0.5);
  color: #00d4ff;
  backdrop-filter: blur(5px);
}

.form-group select:focus, .form-group input[type="range"]:focus {
  outline: none;
  border-color: #00d4ff;
  box-shadow: 0 0 15px rgba(0, 212, 255, 0.3);
}

.generate-btn, .recognize-btn, .remix-btn, .upload-btn, .download-btn {
  background: linear-gradient(45deg, #00d4ff, #ff00ff);
  color: white;
  border: none;
  border-radius: 6px;
  padding: 12px 20px;
  cursor: pointer;
  font-size: 16px;
  width: 100%;
  transition: all 0.3s ease;
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.3);
}

.generate-btn:hover, .recognize-btn:hover, .remix-btn:hover, .upload-btn:hover, .download-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.5);
}

.generate-btn:disabled, .recognize-btn:disabled, .remix-btn:disabled, .upload-btn:disabled {
  background: rgba(10, 10, 10, 0.5);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
  border: 1px solid rgba(0, 212, 255, 0.2);
}

.upload-area {
  margin-bottom: 20px;
}

.drop-zone {
  border: 2px dashed rgba(0, 212, 255, 0.3);
  border-radius: 8px;
  padding: 40px 20px;
  text-align: center;
  transition: all 0.3s ease;
  background: rgba(10, 10, 10, 0.5);
  backdrop-filter: blur(5px);
}

.drop-zone:hover {
  border-color: #00d4ff;
  background: rgba(0, 212, 255, 0.05);
}

.drop-zone.drag-over {
  border-color: #00ff9d;
  background: rgba(0, 255, 157, 0.05);
  box-shadow: 0 0 30px rgba(0, 255, 157, 0.2);
}

.upload-content p {
  margin-bottom: 15px;
  color: #00d4ff;
}

.file-types {
  font-size: 0.9rem;
  color: #00ff9d;
}

.recognition-result, .playback-section {
  margin-top: 20px;
  padding: 15px;
  background: rgba(10, 10, 10, 0.5);
  border-radius: 4px;
  border-left: 4px solid #00d4ff;
  border: 1px solid rgba(0, 212, 255, 0.2);
  backdrop-filter: blur(5px);
}

.recognition-result h3, .playback-section h3 {
  margin-top: 0;
  color: #00ff9d;
  text-shadow: 0 0 5px rgba(0, 255, 157, 0.3);
}

.recognition-result pre {
  white-space: pre-wrap;
  word-break: break-word;
  margin: 0;
  background: rgba(10, 10, 10, 0.8);
  padding: 10px;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
  color: #00d4ff;
  border: 1px solid rgba(0, 212, 255, 0.2);
}

.playback-section audio {
  width: 100%;
  margin-bottom: 10px;
  background: rgba(10, 10, 10, 0.5);
  border-radius: 4px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-music-view {
    padding: 10px;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
  
  .ai-features {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }
  
  .feature-section {
    padding: 1.5rem;
  }
  
  .drop-zone {
    padding: 20px 10px;
  }
}
</style>