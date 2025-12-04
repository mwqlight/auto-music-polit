<template>
  <MainLayout>
    <div class="video-extractor-container">
      <div class="sidebar">
        <div class="sidebar-header">
          <h3>功能导航</h3>
        </div>
        <el-menu
          default-active="1"
          mode="vertical"
          @select="handleMenuSelect"
        >
          <el-menu-item index="1">
            <el-icon><VideoCamera /></el-icon>
            <span>视频提取</span>
          </el-menu-item>
          <el-menu-item index="2">
            <el-icon><Music /></el-icon>
            <span>音乐测试库</span>
          </el-menu-item>
          <el-menu-item index="3">
            <el-icon><Setting /></el-icon>
            <span>设置</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div class="main-panel">
        <el-tabs v-model="activeTab" type="border-card">
          <!-- 视频提取Tab -->
          <el-tab-pane label="视频音频提取" name="extract">
            <div class="extract-content">
              <div class="upload-section">
                <h2>视频文件上传</h2>
                <el-upload
                  class="upload-demo"
                  drag
                  action="#"
                  :auto-upload="false"
                  :on-change="handleFileChange"
                  :file-list="fileList"
                  accept="video/*"
                >
                  <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
                  <div class="el-upload__text">
                    将视频文件拖到此处，或<em>点击选择文件</em>
                  </div>
                  <template #tip>
                    <div class="el-upload__tip">
                      支持格式: MP4, AVI, MKV, MOV, FLV (最大500MB)
                    </div>
                  </template>
                </el-upload>
                <div v-if="uploadProgress > 0" class="progress-container">
                  <el-progress :percentage="uploadProgress" status="active" />
                  <p>上传进度: {{ uploadProgress }}%</p>
                </div>
              </div>

              <div class="url-section">
                <h2>视频链接提取</h2>
                <el-input
                  v-model="videoUrl"
                  placeholder="请输入视频链接 (YouTube, B站, 抖音等)"
                  clearable
                  style="width: 100%; margin-bottom: 10px;"
                >
                  <template #append>
                    <el-button @click="extractFromUrl" :loading="extracting">
                      提取音频
                    </el-button>
                  </template>
                </el-input>
                <div class="test-links">
                  <h3>测试链接 (点击使用):</h3>
                  <el-button-group>
                    <el-button size="small" @click="useTestLink(1)">测试视频1</el-button>
                    <el-button size="small" @click="useTestLink(2)">测试视频2</el-button>
                    <el-button size="small" @click="useTestLink(3)">测试视频3</el-button>
                  </el-button-group>
                </div>
              </div>

              <div v-if="extractedAudio" class="result-section">
                <h2>提取结果</h2>
                <el-card>
                  <div class="audio-info">
                    <h3>{{ extractedAudio.title }}</h3>
                    <p>时长: {{ extractedAudio.duration }}</p>
                    <p>格式: {{ extractedAudio.format }}</p>
                  </div>
                  <div class="audio-controls">
                    <el-button type="primary" @click="playAudio">
                      <el-icon><Play /></el-icon>
                      播放音频
                    </el-button>
                    <el-button @click="downloadAudio">
                      <el-icon><Download /></el-icon>
                      下载音频
                    </el-button>
                  </div>
                </el-card>
              </div>
            </div>
          </el-tab-pane>

          <!-- 音乐测试库Tab -->
          <el-tab-pane label="音乐测试库" name="music-library">
            <div class="music-library-content">
              <h2>内置测试音乐</h2>
              <el-row :gutter="20">
                <el-col :span="8" v-for="(song, index) in testSongs" :key="index">
                  <el-card :body-style="{ padding: '20px' }">
                    <div class="song-info">
                      <h3>{{ song.title }}</h3>
                      <p>{{ song.artist }}</p>
                      <p class="duration">{{ song.duration }}</p>
                    </div>
                    <div class="song-actions">
                      <el-button type="primary" size="small" @click="playTestSong(index)">
                        <el-icon><Play /></el-icon>
                        播放
                      </el-button>
                      <el-button size="small" @click="addToPlaylist(index)">
                        <el-icon><Plus /></el-icon>
                        添加到播放列表
                      </el-button>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </div>
          </el-tab-pane>

          <!-- 设置Tab -->
          <el-tab-pane label="设置" name="settings">
            <div class="settings-content">
              <h2>提取设置</h2>
              <el-form label-width="120px">
                <el-form-item label="音频格式">
                  <el-select v-model="audioFormat" placeholder="请选择">
                    <el-option label="MP3" value="mp3"></el-option>
                    <el-option label="WAV" value="wav"></el-option>
                    <el-option label="FLAC" value="flac"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="音质选择">
                  <el-select v-model="audioQuality" placeholder="请选择">
                    <el-option label="标准 (128kbps)" value="standard"></el-option>
                    <el-option label="高清 (320kbps)" value="high"></el-option>
                    <el-option label="无损" value="lossless"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="输出目录">
                  <el-input v-model="outputDir" placeholder="默认下载目录"></el-input>
                </el-form-item>
                <el-form-item label="自动播放">
                  <el-switch v-model="autoPlay"></el-switch>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </MainLayout>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { ElMessage, ElLoading } from 'element-plus'
import {
  VideoCamera,
  Music,
  Setting,
  UploadFilled,
  Play,
  Download,
  Plus
} from '@element-plus/icons-vue'
import MainLayout from '@/components/layout/MainLayout.vue'
import { uploadVideo, extractAudioFromUrl } from '@/api/modules/crawler'

// 响应式数据
const activeTab = ref('extract')
const fileList = ref<any[]>([])
const videoUrl = ref('')
const uploadProgress = ref(0)
const extracting = ref(false)
const extractedAudio = ref<any>(null)

// 测试数据
const testLinks = [
  'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  'https://www.bilibili.com/video/BV1xx411c7XW',
  'https://www.douyin.com/video/7000000000000000000'
]

const testSongs = [
  {
    title: '测试歌曲1',
    artist: '测试艺术家A',
    duration: '3:45',
    url: 'https://example.com/song1.mp3'
  },
  {
    title: '测试歌曲2',
    artist: '测试艺术家B',
    duration: '4:20',
    url: 'https://example.com/song2.mp3'
  },
  {
    title: '测试歌曲3',
    artist: '测试艺术家C',
    duration: '2:55',
    url: 'https://example.com/song3.mp3'
  },
  {
    title: '测试歌曲4',
    artist: '测试艺术家D',
    duration: '5:10',
    url: 'https://example.com/song4.mp3'
  },
  {
    title: '测试歌曲5',
    artist: '测试艺术家E',
    duration: '3:30',
    url: 'https://example.com/song5.mp3'
  },
  {
    title: '测试歌曲6',
    artist: '测试艺术家F',
    duration: '4:50',
    url: 'https://example.com/song6.mp3'
  }
]

// 设置数据
const audioFormat = ref('mp3')
const audioQuality = ref('high')
const outputDir = ref('')
const autoPlay = ref(false)

// 方法
const handleMenuSelect = (index: string) => {
  console.log('Menu selected:', index)
}

const handleFileChange = (file: any) => {
  if (file.raw) {
    const loading = ElLoading.service({
      lock: true,
      text: '正在上传视频...',
      background: 'rgba(0, 0, 0, 0.7)'
    })

    // 模拟上传进度
    const interval = setInterval(() => {
      uploadProgress.value += 10
      if (uploadProgress.value >= 100) {
        clearInterval(interval)
        uploadProgress.value = 100
        loading.close()
        ElMessage.success('视频上传成功')
        
        // 模拟音频提取结果
        setTimeout(() => {
          extractedAudio.value = {
            title: file.raw.name.replace(/\.[^/.]+$/, ''),
            duration: '3:20',
            format: 'MP3',
            url: 'https://example.com/extracted_audio.mp3'
          }
          ElMessage.success('音频提取完成')
        }, 1000)
      }
    }, 300)
  }
}

const extractFromUrl = async () => {
  if (!videoUrl.value) {
    ElMessage.warning('请输入视频链接')
    return
  }

  extracting.value = true
  try {
    const response = await extractAudioFromUrl(videoUrl.value)
    extractedAudio.value = response.data
    ElMessage.success('音频提取成功')
  } catch (error) {
    ElMessage.error('音频提取失败，请检查链接是否有效')
  } finally {
    extracting.value = false
  }
}

const useTestLink = (index: number) => {
  videoUrl.value = testLinks[index - 1]
  ElMessage.success('已加载测试链接')
}

const playAudio = () => {
  if (extractedAudio.value) {
    ElMessage.success('正在播放音频')
    // 这里可以添加实际的音频播放逻辑
  }
}

const downloadAudio = () => {
  if (extractedAudio.value) {
    ElMessage.success('开始下载音频')
    // 这里可以添加实际的下载逻辑
  }
}

const playTestSong = (index: number) => {
  const song = testSongs[index]
  ElMessage.success(`正在播放: ${song.title}`)
  // 这里可以添加实际的音频播放逻辑
}

const addToPlaylist = (index: number) => {
  const song = testSongs[index]
  ElMessage.success(`已添加到播放列表: ${song.title}`)
  // 这里可以添加实际的添加播放列表逻辑
}
</script>

<style scoped>
.video-extractor-container {
  display: flex;
  gap: 20px;
  height: 100%;
}

.sidebar {
  width: 250px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.sidebar-header h3 {
  margin: 0;
  font-size: 16px;
  color: #333;
}

.main-panel {
  flex: 1;
  min-height: 600px;
}

.extract-content {
  padding: 20px;
}

.upload-section,
.url-section,
.result-section {
  margin-bottom: 30px;
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.upload-section h2,
.url-section h2,
.result-section h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}

.progress-container {
  margin-top: 15px;
}

.test-links {
  margin-top: 15px;
}

.test-links h3 {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
}

.audio-info h3 {
  margin: 0 0 10px 0;
  font-size: 16px;
}

.audio-info p {
  margin: 5px 0;
  color: #666;
}

.audio-controls {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.music-library-content {
  padding: 20px;
}

.music-library-content h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}

.song-info h3 {
  margin: 0 0 5px 0;
  font-size: 14px;
}

.song-info p {
  margin: 3px 0;
  color: #666;
  font-size: 12px;
}

.song-info .duration {
  color: #999;
}

.song-actions {
  margin-top: 15px;
  display: flex;
  gap: 10px;
}

.settings-content {
  padding: 20px;
}

.settings-content h2 {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}
</style>