import request from '@/api'

// 音乐识别API
export const recognizeMusic = (audioFile: File) => {
  const formData = new FormData()
  formData.append('audio', audioFile)
  
  return request<string>({
    url: '/api/v1/ai-music/recognize',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// AI混音API
export const remixMusic = (audioFile: File, style: string) => {
  const formData = new FormData()
  formData.append('audio', audioFile)
  formData.append('style', style)
  
  return request<Blob>({
    url: '/api/v1/ai-music/remix',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    responseType: 'blob'
  })
}

// AI音乐生成API
export const generateMusic = (params: {
  style: string
  mood: string
  duration: number
}) => {
  return request<Blob>({
    url: '/api/v1/ai-music/generate',
    method: 'post',
    data: params,
    responseType: 'blob'
  })
}