import { defineStore } from 'pinia'
import { Music } from '@/types/models/music'
import { getMusicList, searchMusic, getAllQualities } from '@/api/modules/music'

interface MusicState {
  musicList: Music[]
  currentMusic: Music | null
  loading: boolean
  error: string | null
}

export const useMusicStore = defineStore('music', {
  state: (): MusicState => ({
    musicList: [],
    currentMusic: null,
    loading: false,
    error: null
  }),
  
  getters: {
    getMusicById: (state) => (id: number) => {
      return state.musicList.find(music => music.id === id)
    }
  },
  
  actions: {
    async fetchMusicList() {
      this.loading = true
      try {
        const response = await getMusicList()
        this.musicList = response.content
        this.error = null
      } catch (error) {
        this.error = '获取音乐列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async searchMusic(keyword: string) {
      this.loading = true
      try {
        const response = await searchMusic(keyword)
        this.musicList = response.content
        this.error = null
      } catch (error) {
        this.error = '搜索音乐失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    setCurrentMusic(music: Music) {
      this.currentMusic = music
    },
    
    async fetchAllQualities() {
      this.loading = true
      try {
        const response = await getAllQualities()
        this.error = null
        return response
      } catch (error) {
        this.error = '获取音乐质量列表失败'
        console.error(error)
        return []
      } finally {
        this.loading = false
      }
    }
  }
})