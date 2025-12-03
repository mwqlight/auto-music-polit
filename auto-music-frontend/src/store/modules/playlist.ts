import { defineStore } from 'pinia'
import { Playlist } from '@/types/models/playlist'
import { getPlaylistsByUserId, createPlaylist, updatePlaylist, deletePlaylist } from '@/api/modules/playlist'

interface PlaylistState {
  playlists: Playlist[]
  currentPlaylist: Playlist | null
  loading: boolean
  error: string | null
}

export const usePlaylistStore = defineStore('playlist', {
  state: (): PlaylistState => ({
    playlists: [],
    currentPlaylist: null,
    loading: false,
    error: null
  }),
  
  actions: {
    async fetchPlaylistsByUserId(userId: number) {
      this.loading = true
      try {
        const response = await getPlaylistsByUserId(userId)
        this.playlists = response.data
        this.error = null
      } catch (error) {
        this.error = '获取播放列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async createNewPlaylist(playlistData: Partial<Playlist>) {
      this.loading = true
      try {
        const response = await createPlaylist(playlistData)
        this.playlists.push(response.data)
        this.error = null
        return response.data
      } catch (error) {
        this.error = '创建播放列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async updateExistingPlaylist(id: number, playlistData: Partial<Playlist>) {
      this.loading = true
      try {
        const response = await updatePlaylist(id, playlistData)
        const index = this.playlists.findIndex(p => p.id === id)
        if (index !== -1) {
          this.playlists[index] = response.data
        }
        this.error = null
        return response.data
      } catch (error) {
        this.error = '更新播放列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async deleteExistingPlaylist(id: number) {
      this.loading = true
      try {
        await deletePlaylist(id)
        this.playlists = this.playlists.filter(p => p.id !== id)
        this.error = null
      } catch (error) {
        this.error = '删除播放列表失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    setCurrentPlaylist(playlist: Playlist) {
      this.currentPlaylist = playlist
    }
  }
})