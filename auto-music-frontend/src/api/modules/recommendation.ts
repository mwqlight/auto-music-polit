import request from '@/api'
import { Music } from '@/types/models/music'

// 获取用户推荐音乐
export const getRecommendationsForUser = (userId: number, limit: number = 10) => {
  return request<Music[]>({
    url: `/api/v1/recommendations/user/${userId}`,
    method: 'get',
    params: { limit }
  })
}

// 获取热门推荐音乐
export const getPopularRecommendations = (limit: number = 10) => {
  return request<Music[]>({
    url: '/api/v1/recommendations/popular',
    method: 'get',
    params: { limit }
  })
}