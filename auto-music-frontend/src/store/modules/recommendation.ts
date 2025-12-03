import { defineStore } from 'pinia'
import { Music } from '@/types/models/music'
import { getRecommendationsForUser, getPopularRecommendations } from '@/api/modules/recommendation'

interface RecommendationState {
  recommendedMusic: Music[]
  popularMusic: Music[]
  loading: boolean
  error: string | null
}

export const useRecommendationStore = defineStore('recommendation', {
  state: (): RecommendationState => ({
    recommendedMusic: [],
    popularMusic: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchRecommendationsForUser(userId: number, limit: number = 10) {
      this.loading = true
      try {
        const response = await getRecommendationsForUser(userId, limit)
        this.recommendedMusic = response.data
        this.error = null
      } catch (error) {
        this.error = '获取推荐音乐失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async fetchPopularRecommendations(limit: number = 10) {
      this.loading = true
      try {
        const response = await getPopularRecommendations(limit)
        this.popularMusic = response.data
        this.error = null
      } catch (error) {
        this.error = '获取热门音乐失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    }
  }
})