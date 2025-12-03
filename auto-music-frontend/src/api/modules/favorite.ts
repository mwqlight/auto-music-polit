import request from '@/api'

// 获取用户的收藏列表
export const getFavoritesByUserId = (userId: number) => {
  return request<any[]>({
    url: `/api/v1/favorites/user/${userId}`,
    method: 'get'
  })
}

// 添加到收藏夹
export const addToFavorites = (userId: number, musicId: number) => {
  return request({
    url: '/api/v1/favorites',
    method: 'post',
    params: { userId, musicId }
  })
}

// 从收藏夹移除
export const removeFromFavorites = (userId: number, musicId: number) => {
  return request({
    url: '/api/v1/favorites',
    method: 'delete',
    params: { userId, musicId }
  })
}

// 检查是否已收藏
export const isFavorite = (userId: number, musicId: number) => {
  return request<boolean>({
    url: '/api/v1/favorites/check',
    method: 'get',
    params: { userId, musicId }
  })
}