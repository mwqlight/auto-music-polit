import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView.vue')
  },
  {
    path: '/music-library',
    name: 'musicLibrary',
    component: () => import('@/views/MusicLibraryView.vue')
  },
  {
    path: '/ai-music',
    name: 'aiMusic',
    component: () => import('@/views/AiMusicView.vue')
  },
  {
    path: '/playlists',
    name: 'playlists',
    component: () => import('@/views/PlaylistsView.vue')
  },
  {
    path: '/favorites',
    name: 'favorites',
    component: () => import('@/views/FavoritesView.vue')
  },
  {
    path: '/music-crawler',
    name: 'musicCrawler',
    component: () => import('@/views/MusicCrawlerView.vue')
  },
  {
    path: '/recommendations',
    name: 'recommendations',
    component: () => import('@/views/RecommendationsView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router