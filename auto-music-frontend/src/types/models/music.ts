export interface Music {
  id: number
  title: string
  artist: string
  filePath: string
  durationSeconds: number
  createdAt: string
  updatedAt: string
  audioFingerprint?: string
  aiGenerated?: boolean
  genre?: string
  mood?: string
}