import request from '@/api'
import { Music } from '@/types/models/music'

// 获取所有音乐
export const getMusicList = () => {
  return request<{ content: Music[] }>({
    url: '/api/v1/music',
    method: 'get'
  })
}

// 根据ID获取音乐
export const getMusicById = (id: number) => {
  return request<Music>({
    url: `/api/v1/music/${id}`,
    method: 'get'
  })
}

// 创建音乐
export const createMusic = (data: Partial<Music>) => {
  return request<Music>({
    url: '/api/v1/music',
    method: 'post',
    data
  })
}

// 更新音乐
export const updateMusic = (id: number, data: Partial<Music>) => {
  return request<Music>({
    url: `/api/v1/music/${id}`,
    method: 'put',
    data
  })
}

// 删除音乐
export const deleteMusic = (id: number) => {
  return request<void>({
    url: `/api/v1/music/${id}`,
    method: 'delete'
  })
}

// 搜索音乐
export const searchMusic = (keyword: string, page: number = 0, size: number = 10) => {
  return request<{ content: Music[] }>({
    url: `/api/v1/music/search`,
    method: 'get',
    params: { query: keyword, page, size }
  })
}

// 根据音频指纹查找音乐
export const getMusicByFingerprint = (fingerprint: string) => {
  return request<Music[]>({
    url: `/api/v1/music/fingerprint/${fingerprint}`,
    method: 'get'
  })
}

// 根据风格查找音乐
export const getMusicByGenre = (genre: string) => {
  return request<{ content: Music[] }>({
    url: `/api/v1/music/genre/${genre}`,
    method: 'get'
  })
}

// 根据情绪查找音乐
export const getMusicByMood = (mood: string) => {
  return request<{ content: Music[] }>({
    url: `/api/v1/music/mood/${mood}`,
    method: 'get'
  })
}

// 获取AI生成的音乐
export const getAiGeneratedMusic = () => {
  return request<{ content: Music[] }>({
    url: `/api/v1/music/ai-generated`,
    method: 'get'
  })
}

// 获取所有音乐质量
export const getAllQualities = () => {
  return request<string[]>({
    url: `/api/v1/music/qualities`,
    method: 'get'
  })
}