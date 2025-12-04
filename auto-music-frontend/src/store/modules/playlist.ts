import { defineStore } from 'pinia'
import { Playlist } from '@/types/models/playlist'
import { getPlaylistsByUserId, createPlaylist, updatePlaylist, deletePlaylist } from '@/api/modules/playlist'

interface PlaylistState {
  playlists: Playlist[]
  currentPlaylist: Playlist | null
  loading: boolean
  error: string | null
}

// 初始化数据
const initialPlaylists: Playlist[] = [
  {
    id: 1,
    name: '最爱音乐',
    description: '我的最爱音乐集合',
    musics: [
      {
        id: 1,
        title: '青花瓷',
        artist: '周杰伦',
        album: '我很忙',
        duration: 282,
        url: 'https://example.com/music/blue-and-white-porcelain.mp3',
        cover: 'https://example.com/cover/blue-and-white-porcelain.jpg',
        lyric: 'https://example.com/lyric/blue-and-white-porcelain.lrc'
      },
      {
        id: 2,
        title: '晴天',
        artist: '周杰伦',
        album: '叶惠美',
        duration: 267,
        url: 'https://example.com/music/sunny-day.mp3',
        cover: 'https://example.com/cover/sunny-day.jpg',
        lyric: 'https://example.com/lyric/sunny-day.lrc'
      }
    ],
    cover: 'https://example.com/playlist/1.jpg',
    createTime: new Date('2023-01-01'),
    updateTime: new Date('2023-01-01')
  },
  {
    id: 2,
    name: '工作必备',
    description: '工作时听的音乐',
    musics: [
      {
        id: 3,
        title: '卡农',
        artist: '帕赫贝尔',
        album: '卡农与吉格',
        duration: 300,
        url: 'https://example.com/music/canon.mp3',
        cover: 'https://example.com/cover/canon.jpg',
        lyric: 'https://example.com/lyric/canon.lrc'
      },
      {
        id: 4,
        title: '致爱丽丝',
        artist: '贝多芬',
        album: '致爱丽丝',
        duration: 150,
        url: 'https://example.com/music/for-elise.mp3',
        cover: 'https://example.com/cover/for-elise.jpg',
        lyric: 'https://example.com/lyric/for-elise.lrc'
      }
    ],
    cover: 'https://example.com/playlist/2.jpg',
    createTime: new Date('2023-02-01'),
    updateTime: new Date('2023-02-01')
  },
  {
    id: 3,
    name: '健身计划',
    description: '健身时听的音乐',
    musics: [
      {
        id: 5,
        title: 'Eye of the Tiger',
        artist: 'Survivor',
        album: 'Eye of the Tiger',
        duration: 246,
        url: 'https://example.com/music/eye-of-the-tiger.mp3',
        cover: 'https://example.com/cover/eye-of-the-tiger.jpg',
        lyric: 'https://example.com/lyric/eye-of-the-tiger.lrc'
      },
      {
        id: 6,
        title: 'Stronger',
        artist: 'Kanye West',
        album: 'Graduation',
        duration: 311,
        url: 'https://example.com/music/stronger.mp3',
        cover: 'https://example.com/cover/stronger.jpg',
        lyric: 'https://example.com/lyric/stronger.lrc'
      }
    ],
    cover: 'https://example.com/playlist/3.jpg',
    createTime: new Date('2023-03-01'),
    updateTime: new Date('2023-03-01')
  }
]

export const usePlaylistStore = defineStore('playlist', {
  state: (): PlaylistState => ({
    playlists: initialPlaylists,
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