import { Music } from './music'
import { User } from './user'

export interface Playlist {
  id: number
  name: string
  description: string
  user: User
  musics: Music[]
  createdAt: string
  updatedAt: string
}