import request from '@/api'
import { User } from '@/types/models/user'

// 获取所有用户
export const getUserList = () => {
  return request<User[]>({
    url: '/api/v1/users',
    method: 'get'
  })
}

// 根据ID获取用户
export const getUserById = (id: number) => {
  return request<User>({
    url: `/api/v1/users/${id}`,
    method: 'get'
  })
}

// 创建用户
export const createUser = (data: Partial<User>) => {
  return request<User>({
    url: '/api/v1/users',
    method: 'post',
    data
  })
}

// 更新用户
export const updateUser = (id: number, data: Partial<User>) => {
  return request<User>({
    url: `/api/v1/users/${id}`,
    method: 'put',
    data
  })
}

// 删除用户
export const deleteUser = (id: number) => {
  return request<void>({
    url: `/api/v1/users/${id}`,
    method: 'delete'
  })
}

// 用户登录
export const login = (username: string, password: string) => {
  return request<{ token: string }>({
    url: '/api/v1/auth/login',
    method: 'post',
    data: { username, password }
  })
}

// 用户注册
export const register = (username: string, email: string, password: string) => {
  return request<{ message: string }>({
    url: '/api/v1/auth/register',
    method: 'post',
    data: { username, email, password }
  })
}