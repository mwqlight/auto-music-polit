import { defineStore } from 'pinia'
import { Favorite } from '@/types/models/favorite'
import { getFavoritesByUserId, addToFavorites, removeFromFavorites, isFavorite } from '@/api/modules/favorite'

interface FavoriteState {
  favorites: Favorite[]
  loading: boolean
  error: string | null
}

export const useFavoriteStore = defineStore('favorite', {
  state: (): FavoriteState => ({
    favorites: [],
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchFavoritesByUserId(userId: number) {
      this.loading = true
      try {
        const response = await getFavoritesByUserId(userId)
        this.favorites = response.data
        this.error = null
      } catch (error) {
        this.error = '获取收藏列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async addMusicToFavorites(userId: number, musicId: number) {
      this.loading = true
      try {
        await addToFavorites(userId, musicId)
        // 重新获取收藏列表
        await this.fetchFavoritesByUserId(userId)
        this.error = null
      } catch (error) {
        this.error = '添加收藏失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async removeMusicFromFavorites(userId: number, musicId: number) {
      this.loading = true
      try {
        await removeFromFavorites(userId, musicId)
        // 重新获取收藏列表
        await this.fetchFavoritesByUserId(userId)
        this.error = null
      } catch (error) {
        this.error = '取消收藏失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async checkIfFavorite(userId: number, musicId: number) {
      try {
        const response = await isFavorite(userId, musicId)
        return response.data
      } catch (error) {
        console.error(error)
        return false
      }
    },
    
    // 新增方法
    toggleFavorite(music: any) {
      // 这里应该实现切换收藏状态的逻辑
      console.log('Toggle favorite for music:', music)
    },
    
    isFavorite(musicId: number) {
      // 这里应该实现检查是否收藏的逻辑
      return this.favorites.some(fav => fav.musicId === musicId)
    }
  }
})