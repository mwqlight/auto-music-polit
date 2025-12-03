import { defineStore } from 'pinia'
import { Music } from '@/types/models/music'
import { getMusicList, searchMusic } from '@/api/modules/music'

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
        this.musicList = response.data
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
        this.musicList = response.data
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
    }
  }
})