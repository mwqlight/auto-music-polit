import request from '@/api'
import { Playlist } from '@/types/models/playlist'

// 获取所有播放列表
export const getPlaylistList = () => {
  return request<Playlist[]>({
    url: '/api/v1/playlists',
    method: 'get'
  })
}

// 根据ID获取播放列表
export const getPlaylistById = (id: number) => {
  return request<Playlist>({
    url: `/api/v1/playlists/${id}`,
    method: 'get'
  })
}

// 获取用户的所有播放列表
export const getPlaylistsByUserId = (userId: number) => {
  return request<Playlist[]>({
    url: `/api/v1/playlists/user/${userId}`,
    method: 'get'
  })
}

// 创建播放列表
export const createPlaylist = (data: Partial<Playlist>) => {
  return request<Playlist>({
    url: '/api/v1/playlists',
    method: 'post',
    data
  })
}

// 更新播放列表
export const updatePlaylist = (id: number, data: Partial<Playlist>) => {
  return request<Playlist>({
    url: `/api/v1/playlists/${id}`,
    method: 'put',
    data
  })
}

// 删除播放列表
export const deletePlaylist = (id: number) => {
  return request<void>({
    url: `/api/v1/playlists/${id}`,
    method: 'delete'
  })
}