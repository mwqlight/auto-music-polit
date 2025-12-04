import request from '@/api'
import { Theme } from '@/types/models/theme'

// 获取当前主题
export const getTheme = () => {
  return request({
    url: '/api/v1/theme/current',
    method: 'get'
  })
}

// 设置主题
export const setTheme = (theme: string) => {
  return request({
    url: '/api/v1/theme/set',
    method: 'post',
    params: { theme }
  })
}

// 获取所有可用主题
export const getAvailableThemes = () => {
  return request({
    url: '/api/v1/theme/available',
    method: 'get'
  })
}