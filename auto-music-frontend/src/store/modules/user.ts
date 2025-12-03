import { defineStore } from 'pinia'
import { User } from '@/types/models/user'
import { login, register } from '@/api/modules/user'

interface UserState {
  currentUser: User | null
  isAuthenticated: boolean
  loading: boolean
  error: string | null
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    currentUser: null,
    isAuthenticated: false,
    loading: false,
    error: null
  }),
  
  actions: {
    async login(username: string, password: string) {
      this.loading = true
      try {
        const response = await login(username, password)
        // 在实际应用中，你需要处理token存储
        // localStorage.setItem('token', response.data.token)
        this.isAuthenticated = true
        this.error = null
        return response.data
      } catch (error) {
        this.error = '登录失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    async register(username: string, email: string, password: string) {
      this.loading = true
      try {
        const response = await register(username, email, password)
        this.error = null
        return response.data
      } catch (error) {
        this.error = '注册失败'
        console.error(error)
      } finally {
        this.loading = false
      }
    },
    
    logout() {
      // 清除用户信息
      this.currentUser = null
      this.isAuthenticated = false
      // 清除token
      // localStorage.removeItem('token')
    }
  }
})