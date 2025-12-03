import { Music } from './music'
import { User } from './user'

export interface Favorite {
  id: number
  user: User
  music: Music
  createdAt: string
}