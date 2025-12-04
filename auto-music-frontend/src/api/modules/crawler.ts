import request from '@/api'
import { Music } from '@/types/models/music'

// 从单个音乐源爬取音乐
export const crawlSingleSource = (sourceUrl: string, sourceType: string = 'general') => {
  return request({
    url: '/api/v1/crawler/crawl/single',
    method: 'post',
    params: { sourceUrl, sourceType }
  })
}

// 从多个音乐源爬取音乐
export const crawlMultipleSources = (sourceMap: Record<string, string>) => {
  return request({
    url: '/api/v1/crawler/crawl/multiple',
    method: 'post',
    data: sourceMap
  })
}

// 获取支持的音乐源类型
export const getSupportedSourceTypes = () => {
  return request({
    url: '/api/v1/crawler/source-types',
    method: 'get'
  })
}

// 立即执行爬取任务
export const executeCrawlingJobNow = () => {
  return request({
    url: '/api/v1/crawler/execute-now',
    method: 'post'
  })
}

// 爬取所有支持的音乐源
export const crawlAllSupportedSources = () => {
  return request({
    url: '/api/v1/crawler/crawl-all',
    method: 'post'
  })
}

// 调度爬取任务
export const scheduleCrawlingJob = (cronExpression: string) => {
  return request({
    url: '/api/v1/crawler/schedule',
    method: 'post',
    params: { cronExpression }
  })
}

// 执行立即爬取（测试用）
export const crawlImmediate = (sourceUrl: string, sourceType: string = 'general') => {
  return request({
    url: '/api/v1/crawler/crawl/immediate',
    method: 'post',
    params: { sourceUrl, sourceType }
  })
}

// 从本地视频文件提取音乐
export const extractMusicFromVideoFile = (videoFile: File) => {
  const formData = new FormData()
  formData.append('videoFile', videoFile)
  
  return request({
    url: '/api/v1/crawler/extract/video-file',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 从视频链接提取音乐
export const extractMusicFromVideoUrl = (videoUrl: string) => {
  return request({
    url: '/api/v1/crawler/extract/video-url',
    method: 'post',
    params: { videoUrl }
  })
}

// 音乐搜索功能
export const searchMusic = (keyword: string) => {
  return request({
    url: '/api/v1/crawler/search',
    method: 'get',
    params: { keyword }
  })
}

// 音频剪切功能
export const cutAudio = (audioFile: File, startTime: number, endTime: number) => {
  const formData = new FormData()
  formData.append('audioFile', audioFile)
  formData.append('startTime', startTime.toString())
  formData.append('endTime', endTime.toString())
  
  return request({
    url: '/api/v1/crawler/edit/cut',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 添加音效功能
export const addAudioEffect = (audioFile: File, effectType: string) => {
  const formData = new FormData()
  formData.append('audioFile', audioFile)
  formData.append('effectType', effectType)
  
  return request({
    url: '/api/v1/crawler/edit/effect',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
