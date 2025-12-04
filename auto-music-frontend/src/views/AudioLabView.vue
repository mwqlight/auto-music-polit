<template>
  <div class="audio-lab-container">
    <div class="audio-lab-header">
      <h1 class="page-title">音频实验室</h1>
      <p class="page-subtitle">探索音乐的无限可能，释放你的创造力</p>
    </div>

    <el-tabs v-model="activeTab" type="border-card" class="audio-lab-tabs">
      <!-- 视频转音频 -->
      <el-tab-pane label="视频转音频" name="video-to-audio">
        <div class="tab-content">
          <el-card class="audio-lab-card">
            <template #header>
              <div class="card-header">
                <i class="el-icon-video-play"></i>
                <span>从视频中提取音频</span>
              </div>
            </template>
            
            <el-tabs v-model="videoTabActive" type="card">
              <el-tab-pane label="上传视频文件" name="file">
                <el-upload
                  class="video-uploader"
                  drag
                  action=""
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleVideoFileUpload"
                  accept="video/*"
                >
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将视频文件拖到此处，或<em>点击选择</em></div>
                  <div class="el-upload__tip" slot="tip">支持 MP4、AVI、MOV 等视频格式，文件大小不超过100MB</div>
                </el-upload>
                <div v-if="selectedVideoFile" class="selected-file-info">
                  <i class="el-icon-document"></i>
                  <span>{{ selectedVideoFile.name }}</span>
                  <span class="file-size">({{ formatFileSize(selectedVideoFile.size) }})</span>
                  <el-button type="text" icon="el-icon-delete" @click="clearSelectedVideoFile">
                    移除
                  </el-button>
                </div>
                <el-button
                  type="primary"
                  class="extract-button"
                  :loading="extractingFromFile"
                  :disabled="!selectedVideoFile"
                  @click="extractFromVideoFile"
                >
                  <i class="el-icon-sound"></i>
                  提取音频
                </el-button>
              </el-tab-pane>
              
              <el-tab-pane label="视频链接" name="url">
                <div class="video-url-section">
                  <el-input
                    v-model="videoUrl"
                    placeholder="请输入视频链接 (支持 YouTube, B站, 腾讯视频等)"
                    class="video-url-input"
                    clearable
                    style="margin-bottom: 20px"
                  />
                  
                  <!-- 测试链接 -->
                  <div class="test-links">
                    <el-divider content-position="left">📌 测试链接</el-divider>
                    <div class="test-link-buttons">
                      <el-button 
                        type="text" 
                        size="small"
                        @click="useTestLink('youtube')"
                      >
                        YouTube 测试视频
                      </el-button>
                      <el-button 
                        type="text" 
                        size="small"
                        @click="useTestLink('bilibili')"
                      >
                        B站 测试视频
                      </el-button>
                      <el-button 
                        type="text" 
                        size="small"
                        @click="useTestLink('tencent')"
                      >
                        腾讯视频 测试
                      </el-button>
                    </div>
                  </div>
                  
                  <el-button
                    type="primary"
                    class="extract-button"
                    :loading="extractingFromUrl"
                    :disabled="!videoUrl"
                    @click="extractFromVideoUrl"
                  >
                    <i class="el-icon-link"></i>
                    提取音频
                  </el-button>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 音频编辑 -->
      <el-tab-pane label="音频编辑" name="audio-edit">
        <div class="tab-content">
          <el-card class="audio-lab-card">
            <template #header>
              <div class="card-header">
                <i class="el-icon-edit"></i>
                <span>音频编辑工具</span>
              </div>
            </template>
            
            <div class="audio-edit-section">
              <!-- 音频上传 -->
              <div class="audio-upload-area">
                <el-upload
                  class="audio-uploader"
                  drag
                  action=""
                  :auto-upload="false"
                  :show-file-list="false"
                  :on-change="handleAudioFileUpload"
                  accept="audio/*"
                >
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将音频文件拖到此处，或<em>点击选择</em></div>
                  <div class="el-upload__tip" slot="tip">支持 MP3、WAV、FLAC 等音频格式</div>
                </el-upload>
                
                <!-- 测试音频 -->
                <div class="test-audio-section">
                  <el-divider content-position="left">🎵 测试音频</el-divider>
                  <div class="test-audio-list">
                    <div 
                      v-for="audio in testAudios" 
                      :key="audio.id"
                      class="test-audio-item"
                      @click="selectTestAudio(audio)"
                    >
                      <i class="el-icon-music"></i>
                      <div class="audio-info">
                        <div class="audio-title">{{ audio.title }}</div>
                        <div class="audio-artist">{{ audio.artist }}</div>
                      </div>
                      <el-button type="text" size="small">选择</el-button>
                    </div>
                  </div>
                </div>
              </div>
              
              <div v-if="selectedAudioFile" class="selected-file-info">
                <i class="el-icon-document"></i>
                <span>{{ selectedAudioFile.name }}</span>
                <el-button type="text" icon="el-icon-delete" @click="clearSelectedAudioFile">
                  移除
                </el-button>
              </div>

              <!-- 音频编辑选项 -->
              <div v-if="selectedAudioFile" class="audio-edit-options">
                <el-tabs v-model="editTabActive" type="card">
                  <!-- 音频剪切 -->
                  <el-tab-pane label="音频剪切" name="cut">
                    <div class="cut-audio-section">
                      <el-form inline>
                        <el-form-item label="开始时间 (秒)">
                          <el-input-number
                            v-model="cutStartTime"
                            :min="0"
                            :max="audioDuration || 1000"
                            placeholder="0"
                            style="width: 120px"
                          />
                        </el-form-item>
                        <el-form-item label="结束时间 (秒)">
                          <el-input-number
                            v-model="cutEndTime"
                            :min="cutStartTime + 1"
                            :max="audioDuration || 1000"
                            placeholder="60"
                            style="width: 120px"
                          />
                        </el-form-item>
                        <el-form-item label="输出格式">
                          <el-select v-model="outputFormat" placeholder="选择格式">
                            <el-option label="MP3" value="mp3" />
                            <el-option label="WAV" value="wav" />
                            <el-option label="FLAC" value="flac" />
                          </el-select>
                        </el-form-item>
                        <el-form-item>
                          <el-button
                            type="primary"
                            :loading="cuttingAudio"
                            :disabled="!canCutAudio"
                            @click="cutAudio"
                          >
                            <i class="el-icon-scissors"></i>
                            剪切音频
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </el-tab-pane>

                  <!-- 添加音效 -->
                  <el-tab-pane label="音效处理" name="effect">
                    <div class="add-effect-section">
                      <el-form inline>
                        <el-form-item label="音效类型">
                          <el-select v-model="selectedEffect" placeholder="请选择音效">
                            <el-option label="增强低音" value="bass_boost" />
                            <el-option label="增强高音" value="treble_boost" />
                            <el-option label="混响效果" value="reverb" />
                            <el-option label="回声效果" value="echo" />
                            <el-option label="变速播放" value="speed" />
                            <el-option label="变调处理" value="pitch" />
                            <el-option label="降噪处理" value="noise_reduction" />
                            <el-option label="均衡器" value="equalizer" />
                          </el-select>
                        </el-form-item>
                        <el-form-item label="强度">
                          <el-slider
                            v-model="effectIntensity"
                            :min="1"
                            :max="10"
                            :disabled="!selectedEffect"
                          />
                        </el-form-item>
                        <el-form-item>
                          <el-button
                            type="primary"
                            :loading="addingEffect"
                            :disabled="!selectedEffect"
                            @click="addAudioEffect"
                          >
                            <i class="el-icon-magic-stick"></i>
                            应用音效
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </el-tab-pane>

                  <!-- 音频合并 -->
                  <el-tab-pane label="音频合并" name="merge">
                    <div class="merge-audio-section">
                      <el-upload
                        class="merge-uploader"
                        drag
                        action=""
                        :auto-upload="false"
                        :show-file-list="true"
                        :on-change="handleMergeAudioUpload"
                        multiple
                        accept="audio/*"
                      >
                        <i class="el-icon-upload"></i>
                        <div class="el-upload__text">上传多个音频文件进行合并</div>
                        <div class="el-upload__tip" slot="tip">支持同时上传多个音频文件</div>
                      </el-upload>
                      <el-button
                        type="primary"
                        class="merge-button"
                        :loading="mergingAudio"
                        :disabled="mergeAudioFiles.length < 2"
                        @click="mergeAudio"
                      >
                        <i class="el-icon-folder-add"></i>
                        合并音频
                      </el-button>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 音乐搜索 -->
      <el-tab-pane label="音乐搜索" name="music-search">
        <div class="tab-content">
          <el-card class="audio-lab-card">
            <template #header>
              <div class="card-header">
                <i class="el-icon-search"></i>
                <span>全网音乐搜索</span>
              </div>
            </template>
            
            <div class="music-search-section">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索歌曲、歌手或专辑名称"
                class="search-input"
                clearable
                style="margin-bottom: 20px"
                @keyup.enter="searchMusic"
              >
                <template #append>
                  <el-button type="primary" @click="searchMusic">
                    <i class="el-icon-search"></i>
                    搜索
                  </el-button>
                </template>
              </el-input>
              
              <!-- 热门搜索 -->
              <div class="hot-searches">
                <el-divider content-position="left">🔥 热门搜索</el-divider>
                <div class="hot-tags">
                  <el-tag 
                    v-for="tag in hotSearchTags" 
                    :key="tag"
                    type="info"
                    @click="searchKeyword = tag"
                  >
                    {{ tag }}
                  </el-tag>
                </div>
              </div>
              
              <!-- 搜索结果 -->
              <div v-if="searchResults.length > 0" class="search-results">
                <el-divider content-position="left">
                  搜索结果 ({{ searchResults.length }})
                </el-divider>
                <div class="search-result-list">
                  <div 
                    v-for="music in searchResults" 
                    :key="music.id"
                    class="search-result-item"
                  >
                    <div class="music-cover">
                      <img :src="music.coverUrl || 'https://picsum.photos/80/80'" alt="封面">
                    </div>
                    <div class="music-details">
                      <div class="music-title">{{ music.title }}</div>
                      <div class="music-artist">{{ music.artist }}</div>
                      <div class="music-album">{{ music.album }}</div>
                    </div>
                    <div class="music-actions">
                      <el-button type="text" icon="el-icon-headset" @click="playPreview(music)">
                        试听
                      </el-button>
                      <el-button type="text" icon="el-icon-download" @click="downloadMusic(music)">
                        下载
                      </el-button>
                      <el-button type="text" icon="el-icon-plus" @click="addToLibrary(music)">
                        收藏
                      </el-button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 处理结果 -->
      <el-tab-pane label="处理结果" name="results">
        <div class="tab-content">
          <el-card class="audio-lab-card" v-if="processingResult">
            <template #header>
              <div class="card-header">
                <i class="el-icon-check"></i>
                <span>处理结果</span>
              </div>
            </template>
            <div class="processing-result">
              <div class="result-info">
                <div class="result-icon">
                  <i class="el-icon-success"></i>
                </div>
                <div class="result-details">
                  <h3>{{ processingResult.title }}</h3>
                  <p>{{ processingResult.message }}</p>
                  <div v-if="processingResult.data" class="result-data">
                    <div class="music-info">
                      <span class="label">标题:</span>
                      <span class="value">{{ processingResult.data.title }}</span>
                    </div>
                    <div class="music-info">
                      <span class="label">艺术家:</span>
                      <span class="value">{{ processingResult.data.artist }}</span>
                    </div>
                    <div class="music-info">
                      <span class="label">流派:</span>
                      <span class="value">{{ processingResult.data.genre }}</span>
                    </div>
                    <div class="music-info">
                      <span class="label">时长:</span>
                      <span class="value">{{ formatDuration(processingResult.data.durationSeconds) }}</span>
                    </div>
                    <div class="music-info">
                      <span class="label">文件大小:</span>
                      <span class="value">{{ formatFileSize(processingResult.data.fileSize) }}</span>
                    </div>
                  </div>
                </div>
              </div>
              <div class="result-actions">
                <el-button type="primary" @click="addToLibrary">
                  <i class="el-icon-plus"></i>
                  添加到音乐库
                </el-button>
                <el-button type="success" @click="downloadResult">
                  <i class="el-icon-download"></i>
                  下载音频
                </el-button>
                <el-button type="info" @click="playResult">
                  <i class="el-icon-headset"></i>
                  立即播放
                </el-button>
              </div>
            </div>
          </el-card>
          
          <div v-else class="empty-results">
            <i class="el-icon-document"></i>
            <p>暂无处理结果</p>
            <p class="empty-desc">请先使用上方功能进行音频处理</p>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  extractMusicFromVideoFile, 
  extractMusicFromVideoUrl, 
  cutAudio as cutAudioApi, 
  addAudioEffect as addAudioEffectApi,
  mergeAudio as mergeAudioApi,
  searchMusic as searchMusicApi 
} from '@/api/modules/crawler'
import { addMusicToLibrary } from '@/api/modules/music'
import { Music } from '@/types/models/music'

// 主标签页
const activeTab = ref('video-to-audio')

// 视频转音频相关
const videoTabActive = ref('file')
const selectedVideoFile = ref<File | null>(null)
const videoUrl = ref('')
const extractingFromFile = ref(false)
const extractingFromUrl = ref(false)

// 音频编辑相关
const editTabActive = ref('cut')
const selectedAudioFile = ref<File | null>(null)
const audioDuration = ref<number | null>(null)
const cutStartTime = ref(0)
const cutEndTime = ref(60)
const outputFormat = ref('mp3')
const selectedEffect = ref('')
const effectIntensity = ref(5)
const cuttingAudio = ref(false)
const addingEffect = ref(false)

// 音频合并相关
const mergeAudioFiles = ref<File[]>([])
const mergingAudio = ref(false)

// 音乐搜索相关
const searchKeyword = ref('')
const searchResults = ref<Music[]>([])
const searchingMusic = ref(false)

// 处理结果
const processingResult = ref<any>(null)

// 测试数据
const testAudios = ref([
  {
    id: 1,
    title: '测试音乐 - 流行歌曲',
    artist: '测试歌手',
    duration: 180,
    url: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3'
  },
  {
    id: 2,
    title: '测试音乐 - 古典音乐',
    artist: '古典乐团',
    duration: 240,
    url: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3'
  },
  {
    id: 3,
    title: '测试音乐 - 电子音乐',
    artist: 'DJ Test',
    duration: 300,
    url: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3'
  }
])

const hotSearchTags = ref([
  '周杰伦', '林俊杰', '陈奕迅', 'Taylor Swift', 'Ed Sheeran',
  '热门歌曲', '最新专辑', '经典老歌', '抖音热歌', '电影原声'
])

const testVideoLinks = {
  youtube: 'https://www.youtube.com/watch?v=dQw4w9WgXcQ',
  bilibili: 'https://www.bilibili.com/video/BV1xx411c7XW',
  tencent: 'https://v.qq.com/x/cover/mzc00200q4w8v7f.html'
}

// 计算属性
const canCutAudio = computed(() => {
  return selectedAudioFile.value && cutStartTime.value >= 0 && cutEndTime.value > cutStartTime.value
})

// 文件大小格式化
const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i]
}

// 时长格式化
const formatDuration = (seconds: number): string => {
  const mins = Math.floor(seconds / 60)
  const secs = Math.floor(seconds % 60)
  return `${mins}:${secs.toString().padStart(2, '0')}`
}

// 使用测试链接
const useTestLink = (type: string) => {
  videoUrl.value = testVideoLinks[type as keyof typeof testVideoLinks]
  ElMessage.success(`已加载${type === 'youtube' ? 'YouTube' : type === 'bilibili' ? 'B站' : '腾讯视频'}测试链接`)
}

// 选择测试音频
const selectTestAudio = (audio: any) => {
  // 创建一个模拟的File对象
  const mockFile = new File([''], `${audio.title}.mp3`, { type: 'audio/mp3' })
  selectedAudioFile.value = mockFile
  audioDuration.value = audio.duration
  ElMessage.success(`已选择测试音频: ${audio.title}`)
}

// 处理视频文件上传
const handleVideoFileUpload = (file: any) => {
  selectedVideoFile.value = file.raw
  ElMessage.success('视频文件选择成功')
}

// 清除选择的视频文件
const clearSelectedVideoFile = () => {
  selectedVideoFile.value = null
}

// 从视频文件提取音频
const extractFromVideoFile = async () => {
  if (!selectedVideoFile.value) return
  
  extractingFromFile.value = true
  try {
    const response = await extractMusicFromVideoFile(selectedVideoFile.value)
    if (response.code === 200) {
      processingResult.value = {
        title: '音频提取成功',
        message: '视频中的音频已成功提取',
        data: response.data
      }
      ElMessage.success('音频提取成功')
      activeTab.value = 'results'
    } else {
      ElMessage.error(response.message || '音频提取失败')
    }
  } catch (error) {
    console.error('音频提取失败:', error)
    ElMessage.error('音频提取失败，请稍后重试')
  } finally {
    extractingFromUrl.value = false
  }
}

// 处理音频文件上传
const handleAudioFileUpload = (file: any) => {
  selectedAudioFile.value = file.raw
  // 模拟获取音频时长
  audioDuration.value = 300 // 5分钟
  ElMessage.success('音频文件选择成功')
}

// 清除选择的音频文件
const clearSelectedAudioFile = () => {
  selectedAudioFile.value = null
  audioDuration.value = null
}

// 处理合并音频上传
const handleMergeAudioUpload = (file: any, fileList: any[]) => {
  mergeAudioFiles.value = fileList.map(f => f.raw)
  ElMessage.success(`已选择 ${fileList.length} 个音频文件`)
}

// 音频剪切
const cutAudio = async () => {
  if (!selectedAudioFile.value) return
  
  cuttingAudio.value = true
  try {
    const formData = new FormData()
    formData.append('audioFile', selectedAudioFile.value)
    formData.append('startTime', cutStartTime.value.toString())
    formData.append('endTime', cutEndTime.value.toString())
    formData.append('format', outputFormat.value)
    
    const response = await cutAudioApi(formData)
    if (response.code === 200) {
      processingResult.value = {
        title: '音频剪切成功',
        message: `音频已从 ${cutStartTime.value}s 剪切到 ${cutEndTime.value}s`,
        data: response.data
      }
      ElMessage.success('音频剪切成功')
      activeTab.value = 'results'
    } else {
      ElMessage.error(response.message || '音频剪切失败')
    }
  } catch (error) {
    console.error('音频剪切失败:', error)
    ElMessage.error('音频剪切失败，请稍后重试')
  } finally {
    cuttingAudio.value = false
  }
}

// 添加音效
const addAudioEffect = async () => {
  if (!selectedAudioFile.value || !selectedEffect.value) return
  
  addingEffect.value = true
  try {
    const formData = new FormData()
    formData.append('audioFile', selectedAudioFile.value)
    formData.append('effectType', selectedEffect.value)
    formData.append('intensity', effectIntensity.value.toString())
    
    const response = await addAudioEffectApi(formData)
    if (response.code === 200) {
      processingResult.value = {
        title: '音效添加成功',
        message: `${getEffectName(selectedEffect.value)} 已成功应用到音频`,
        data: response.data
      }
      ElMessage.success('音效添加成功')
      activeTab.value = 'results'
    } else {
      ElMessage.error(response.message || '音效添加失败')
    }
  } catch (error) {
    console.error('音效添加失败:', error)
    ElMessage.error('音效添加失败，请稍后重试')
  } finally {
    addingEffect.value = false
  }
}

// 获取音效名称
const getEffectName = (effectType: string): string => {
  const effectNames: Record<string, string> = {
    'bass_boost': '增强低音',
    'treble_boost': '增强高音',
    'reverb': '混响效果',
    'echo': '回声效果',
    'speed': '变速播放',
    'pitch': '变调处理',
    'noise_reduction': '降噪处理',
    'equalizer': '均衡器'
  }
  return effectNames[effectType] || effectType
}

// 合并音频
const mergeAudio = async () => {
  if (mergeAudioFiles.value.length < 2) {
    ElMessage.warning('请至少选择2个音频文件进行合并')
    return
  }
  
  mergingAudio.value = true
  try {
    const formData = new FormData()
    mergeAudioFiles.value.forEach(file => {
      formData.append('audioFiles', file)
    })
    formData.append('format', outputFormat.value)
    
    const response = await mergeAudioApi(formData)
    if (response.code === 200) {
      processingResult.value = {
        title: '音频合并成功',
        message: `${mergeAudioFiles.value.length} 个音频文件已成功合并`,
        data: response.data
      }
      ElMessage.success('音频合并成功')
      activeTab.value = 'results'
    } else {
      ElMessage.error(response.message || '音频合并失败')
    }
  } catch (error) {
    console.error('音频合并失败:', error)
    ElMessage.error('音频合并失败，请稍后重试')
  } finally {
    mergingAudio.value = false
  }
}

// 搜索音乐
const searchMusic = async () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }
  
  searchingMusic.value = true
  try {
    const response = await searchMusicApi(searchKeyword.value)
    if (response.code === 200) {
      searchResults.value = response.data
      ElMessage.success(`找到 ${response.data.length} 首歌曲`)
    } else {
      ElMessage.error(response.message || '搜索失败')
    }
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败，请稍后重试')
  } finally {
    searchingMusic.value = false
  }
}

// 播放预览
const playPreview = (music: Music) => {
  ElMessage.success(`正在播放: ${music.title}`)
  // 这里可以添加实际的播放逻辑
}

// 下载音乐
const downloadMusic = (music: Music) => {
  ElMessage.success(`正在下载: ${music.title}`)
  // 这里可以添加实际的下载逻辑
}

// 添加到音乐库
const addToLibrary = async (music?: Music) => {
  try {
    const musicData = music || processingResult.value?.data
    if (!musicData) {
      ElMessage.warning('没有找到要添加的音乐')
      return
    }
    
    const response = await addMusicToLibrary(musicData)
    if (response.code === 200) {
      ElMessage.success('音乐已成功添加到音乐库')
    } else {
      ElMessage.error(response.message || '添加失败')
    }
  } catch (error) {
    console.error('添加到音乐库失败:', error)
    ElMessage.error('添加失败，请稍后重试')
  }
}

// 下载结果
const downloadResult = () => {
  if (!processingResult.value) return
  ElMessage.success('正在下载处理结果...')
  // 这里可以添加实际的下载逻辑
}

// 播放结果
const playResult = () => {
  if (!processingResult.value) return
  ElMessage.success('正在播放处理结果...')
  // 这里可以添加实际的播放逻辑
}

// 从视频链接提取音频
const extractFromVideoUrl = async () => {
  if (!videoUrl.value) return
  
  extractingFromUrl.value = true
  try {
    const response = await extractMusicFromVideoUrl(videoUrl.value)
    if (response.code === 200) {
      processingResult.value = {
        title: '音频提取成功',
        message: '视频中的音频已成功提取',
        data: response.data
      }
      ElMessage.success('音频提取成功')
    } else {
      ElMessage.error(response.message || '音频提取失败')
    }
  } catch (error) {
    console.error('音频提取失败:', error)
    ElMessage.error('音频提取失败，请稍后重试')
  } finally {
    extractingFromUrl.value = false
  }
}

// 处理音频文件上传
const handleAudioFileUpload = (file: any) => {
  selectedAudioFile.value = file.raw
  // 计算音频时长（简化处理，实际应使用音频API）
  const audio = new Audio()
  audio.src = URL.createObjectURL(file.raw)
  audio.onloadedmetadata = () => {
    audioDuration.value = Math.floor(audio.duration)
    cutEndTime.value = Math.min(60, audioDuration.value || 60)
  }
  ElMessage.success('音频文件选择成功')
}

// 清除选择的音频文件
const clearSelectedAudioFile = () => {
  selectedAudioFile.value = null
  audioDuration.value = null
  cutStartTime.value = 0
  cutEndTime.value = 60
  selectedEffect.value = ''
}

// 剪切音频
const cutAudio = async () => {
  if (!selectedAudioFile.value || !canCutAudio.value) return
  
  cuttingAudio.value = true
  try {
    const response = await cutAudioApi(selectedAudioFile.value, cutStartTime.value, cutEndTime.value)
    if (response.code === 200) {
      processingResult.value = {
        title: '音频剪切成功',
        message: `音频已从 ${cutStartTime.value}s 剪切到 ${cutEndTime.value}s`,
        data: { title: selectedAudioFile.value.name.replace(/\.[^/.]+$/, ''), filePath: response.data }
      }
      ElMessage.success('音频剪切成功')
    } else {
      ElMessage.error(response.message || '音频剪切失败')
    }
  } catch (error) {
    console.error('音频剪切失败:', error)
    ElMessage.error('音频剪切失败，请稍后重试')
  } finally {
    cuttingAudio.value = false
  }
}

// 添加音效
const addAudioEffect = async () => {
  if (!selectedAudioFile.value || !selectedEffect.value) return
  
  addingEffect.value = true
  try {
    const response = await addAudioEffectApi(selectedAudioFile.value, selectedEffect.value)
    if (response.code === 200) {
      processingResult.value = {
        title: '音效添加成功',
        message: `已成功为音频添加 "${getEffectName(selectedEffect.value)}" 音效`,
        data: { title: selectedAudioFile.value.name.replace(/\.[^/.]+$/, ''), filePath: response.data }
      }
      ElMessage.success('音效添加成功')
    } else {
      ElMessage.error(response.message || '音效添加失败')
    }
  } catch (error) {
    console.error('音效添加失败:', error)
    ElMessage.error('音效添加失败，请稍后重试')
  } finally {
    addingEffect.value = false
  }
}

// 获取音效名称
const getEffectName = (effectType: string): string => {
  const effectNames: Record<string, string> = {
    'bass_boost': '增强低音',
    'treble_boost': '增强高音',
    'reverb': '混响',
    'echo': '回声',
    'speed': '变速',
    'pitch': '变调'
  }
  return effectNames[effectType] || effectType
}

// 添加到音乐库
const addToLibrary = async () => {
  if (!processingResult.value?.data) return
  
  try {
    const musicData: Partial<Music> = {
      title: processingResult.value.data.title,
      artist: processingResult.value.data.artist || '未知艺术家',
      filePath: processingResult.value.data.filePath,
      genre: processingResult.value.data.genre || '未知',
      durationSeconds: processingResult.value.data.durationSeconds || 0
    }
    
    const response = await addMusicToLibrary(musicData)
    if (response.code === 200) {
      ElMessage.success('已成功添加到音乐库')
    } else {
      ElMessage.error(response.message || '添加到音乐库失败')
    }
  } catch (error) {
    console.error('添加到音乐库失败:', error)
    ElMessage.error('添加到音乐库失败，请稍后重试')
  }
}

// 下载结果
const downloadResult = () => {
  if (!processingResult.value?.data?.filePath) {
    ElMessage.error('没有可下载的音频文件')
    return
  }
  
  // 这里应该调用后端的下载接口
  ElMessage.info('下载功能开发中...')
}

// 格式化时长
const formatDuration = (seconds: number): string => {
  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60
  return `${minutes}:${remainingSeconds.toString().padStart(2, '0')}`
}
</script>

<style scoped>
@import url('https://cdn.jsdelivr.net/npm/font-awesome@4.7.0/css/font-awesome.min.css');

.audio-lab-container {
  min-height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  position: relative;
}

.audio-lab-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 80%, rgba(120, 119, 198, 0.3) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255, 119, 198, 0.3) 0%, transparent 50%),
    radial-gradient(circle at 40% 40%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.audio-lab-header {
  text-align: center;
  margin-bottom: 40px;
  color: white;
  position: relative;
  z-index: 1;
}

.page-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 10px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.page-subtitle {
  font-size: 16px;
  opacity: 0.9;
  font-weight: 300;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 主标签页 */
.audio-lab-tabs {
  max-width: 1400px;
  margin: 0 auto;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 1;
  background: white;
}

.audio-lab-tabs >>> .el-tabs__header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid #dee2e6;
  padding: 0;
}

.audio-lab-tabs >>> .el-tabs__nav-wrap {
  margin: 0;
}

.audio-lab-tabs >>> .el-tabs__nav-wrap::after {
  display: none;
}

.audio-lab-tabs >>> .el-tabs__item {
  font-size: 16px;
  font-weight: 500;
  padding: 16px 24px;
  margin-right: 0;
  border-right: 1px solid #dee2e6;
  transition: all 0.3s ease;
  color: #495057;
}

.audio-lab-tabs >>> .el-tabs__item:hover {
  background: rgba(102, 126, 234, 0.05);
  color: #667eea;
}

.audio-lab-tabs >>> .el-tabs__item:last-child {
  border-right: none;
}

.audio-lab-tabs >>> .el-tabs__item.is-active {
  color: #667eea;
  font-weight: 600;
  background: white;
  border-bottom: 2px solid #667eea;
}

.audio-lab-tabs >>> .el-tabs__active-bar {
  background: #667eea;
  height: 3px;
}

.tab-content {
  background: white;
  padding: 24px;
  min-height: 400px;
}

/* 卡片样式 */
.audio-lab-card {
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  margin-bottom: 24px;
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.audio-lab-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
}

.card-header i {
  color: #667eea;
  font-size: 22px;
  width: 28px;
  text-align: center;
}

.card-header span {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

/* 视频转音频区域 */
.video-to-audio-section {
  padding: 20px 0;
}

.video-uploader {
  margin-bottom: 20px;
}

.video-uploader >>> .el-upload {
  width: 100%;
}

.video-uploader >>> .el-upload-dragger {
  width: 100%;
  border-radius: 8px;
}

.selected-file-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 20px;
  border: 1px solid #e9ecef;
}

.selected-file-info i {
  color: #667eea;
  font-size: 20px;
}

.selected-file-info span {
  flex: 1;
  color: #495057;
  font-weight: 500;
}

.file-size {
  color: #6c757d;
  font-size: 13px;
  font-weight: normal;
}

.extract-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.extract-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.video-url-section {
  padding: 20px 0;
}

.video-url-input {
  margin-bottom: 20px;
}

/* 测试链接 */
.test-links {
  margin-bottom: 20px;
}

.test-link-buttons {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.test-link-buttons .el-button {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  color: #495057;
}

.test-link-buttons .el-button:hover {
  background: #e9ecef;
  border-color: #dee2e6;
}

/* 音频编辑区域 */
.audio-edit-section {
  padding: 20px 0;
}

.audio-upload-area {
  margin-bottom: 20px;
}

.audio-uploader {
  margin-bottom: 20px;
}

.audio-uploader >>> .el-upload {
  width: 100%;
}

.audio-uploader >>> .el-upload-dragger {
  width: 100%;
  border-radius: 8px;
}

/* 测试音频 */
.test-audio-section {
  margin-top: 20px;
}

.test-audio-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.test-audio-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f8f9fa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid transparent;
}

.test-audio-item:hover {
  background: #e9ecef;
  border-color: #dee2e6;
}

.test-audio-item i {
  color: #667eea;
  font-size: 20px;
  flex-shrink: 0;
}

.audio-info {
  flex: 1;
}

.audio-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 2px;
}

.audio-artist {
  font-size: 13px;
  color: #606266;
}

/* 音频编辑选项 */
.audio-edit-options {
  margin-top: 20px;
}

.audio-edit-options >>> .el-tabs__header {
  margin-bottom: 20px;
}

.cut-audio-section,
.add-effect-section,
.merge-audio-section {
  padding: 20px;
  background: #fafafa;
  border-radius: 8px;
}

.merge-uploader {
  margin-bottom: 20px;
}

.merge-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
}

/* 音乐搜索区域 */
.music-search-section {
  padding: 20px 0;
}

.search-input {
  margin-bottom: 20px;
}

.search-input >>> .el-input__inner {
  height: 44px;
  font-size: 15px;
}

/* 热门搜索 */
.hot-searches {
  margin-bottom: 20px;
}

.hot-tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.hot-tags .el-tag {
  cursor: pointer;
  transition: all 0.3s ease;
}

.hot-tags .el-tag:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 搜索结果 */
.search-results {
  margin-top: 20px;
}

.search-result-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.search-result-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.search-result-item:hover {
  background: #e9ecef;
  transform: translateX(4px);
}

.music-cover {
  flex-shrink: 0;
}

.music-cover img {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  object-fit: cover;
}

.music-details {
  flex: 1;
}

.music-title {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  font-size: 15px;
}

.music-artist {
  color: #606266;
  font-size: 14px;
  margin-bottom: 2px;
}

.music-album {
  color: #909399;
  font-size: 13px;
}

.music-actions {
  display: flex;
  gap: 8px;
}

.music-actions .el-button {
  padding: 6px 12px;
  font-size: 13px;
}

/* 处理结果区域 */
.processing-result {
  padding: 20px 0;
}

.result-info {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 24px;
}

.result-icon {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.result-icon i {
  color: white;
  font-size: 32px;
}

.result-details h3 {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
}

.result-details p {
  margin: 0 0 16px 0;
  color: #606266;
  font-size: 14px;
}

.result-data {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.music-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.music-info .label {
  font-weight: 600;
  color: #606266;
  min-width: 80px;
}

.music-info .value {
  color: #303133;
  flex: 1;
}

.result-actions {
  display: flex;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.result-actions .el-button {
  height: 40px;
  padding: 0 24px;
  font-weight: 600;
  border-radius: 6px;
}

/* 空结果 */
.empty-results {
  text-align: center;
  padding: 60px 20px;
  color: #909399;
}

.empty-results i {
  font-size: 64px;
  margin-bottom: 16px;
  opacity: 0.5;
}

.empty-results p {
  margin: 0 0 8px 0;
  font-size: 16px;
}

.empty-desc {
  font-size: 14px;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .audio-lab-container {
    padding: 15px;
  }
  
  .page-title {
    font-size: 28px;
  }
  
  .tab-content {
    padding: 16px;
  }
  
  .audio-lab-tabs >>> .el-tabs__item {
    font-size: 14px;
    padding: 12px 16px;
  }
  
  .result-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .result-actions {
    flex-direction: column;
  }
  
  .result-actions .el-button {
    width: 100%;
  }
  
  .search-result-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .music-cover img {
    width: 100%;
    height: auto;
  }
  
  .music-actions {
    width: 100%;
    justify-content: space-between;
  }
  
  .test-link-buttons {
    flex-direction: column;
  }
  
  .test-link-buttons .el-button {
    width: 100%;
  }
}
</style>