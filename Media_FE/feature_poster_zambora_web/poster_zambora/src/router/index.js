import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '@/views/MainLayout.vue'
import PostersPage from '@/views/PostersPage.vue'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: 'comms/posters', name: 'posters', component: PostersPage },
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
